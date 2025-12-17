package integration.bdd.updateOrderStatus.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

import com.soat.fiap.food.core.order.core.interfaceadapters.bff.controller.web.api.UpdateOrderStatusController;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderReadyEventDto;
import com.soat.fiap.food.core.order.infrastructure.out.event.publisher.azsvcbus.AzSvcBusEventPublisher;

import integration.bdd.common.config.CucumberSpringConfiguration;
import integration.bdd.common.fixture.LocalStackFixture;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.path.json.JsonPath;

/**
 * Steps BDD responsáveis por validar a notificação do cliente quando o pedido
 * estiver pronto para retirada.
 * <p>
 * Executa publicações reais de evento.
 */
public class NotificarUsuarioSteps extends CucumberSpringConfiguration {

	@Autowired
	private AzSvcBusEventPublisher azSvcBusEventPublisher;
	private OrderReadyEventDto orderReadyEventDto;
	private String clientId;
	private String clientMail;
	private String clientName;
	private JsonPath mailResponse;

	/**
	 * Define o e-mail e nome do cliente associado ao pedido.
	 *
	 * @param mail
	 *            e-mail do cliente.
	 * @param name
	 *            nome do cliente.
	 */
	@Dado("que o email do cliente do pedido é {string} e o nome é {string}")
	public void queOEmailDoClienteDoPedidoEEoNomeE(String mail, String name) {
		clientMail = mail;
		clientName = name;

		try {
			clientId = LocalStackFixture.createUser(localstackContainer, cognitoUserPoolId, clientMail, clientName);
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException("Falha ao criar usuário (Cognito) no LocalStack", e);
		}
	}

	/**
	 * Simula a atualização do status do pedido pela cozinha.
	 * <p>
	 * Observação: poderíamos também enviar uma requisição real para o endpoint de
	 * atualização de status do pedido ({@link UpdateOrderStatusController}). Porém,
	 * isso exigiria que as dependências síncronas envolvidas no processo de
	 * validação estivessem UP durante o teste — por exemplo, o serviço de
	 * pagamento.
	 * </p>
	 */
	@Quando("um pedido estiver pronto")
	public void umPedidoEstiverPronto() {
		var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

		orderReadyEventDto = new OrderReadyEventDto();
		orderReadyEventDto.setClientId(clientId);
		orderReadyEventDto.setOrderNumber("ORD-2025-00001");
		orderReadyEventDto.setAmount(new BigDecimal("79.70"));
		orderReadyEventDto.setReadyAt(LocalDateTime.now().format(formatter));

		azSvcBusEventPublisher.publishOrderReadyEvent(orderReadyEventDto);
	}

	/**
	 * Verifica se o sistema notificou o cliente com sucesso.
	 * <p>
	 * Este step consulta a API REST do MailDev para validar o e-mail enviado.
	 * Documentação de referência:
	 * <a href="https://github.com/maildev/maildev/blob/master/docs/rest.md">Mail
	 * Dev REST API</a>
	 * </p>
	 */
	@Entao("o sistema deve notificar o cliente")
	public void oSistemaDeveNotificarOCliente() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		var expectedSubject = String.format("🍔 Seu pedido #%s está pronto!", orderReadyEventDto.getOrderNumber());

		mailResponse = given().queryParam("subject", expectedSubject)
				.when()
				.get(String.format("http://localhost:%d/email", mailDevApiHostPort))
				.then()
				.statusCode(200)
				.extract()
				.jsonPath();

		var emails = mailResponse.getList("");
		assertThat(emails).as("Deve haver pelo menos um e-mail com o assunto esperado: %s", expectedSubject)
				.isNotNull()
				.isNotEmpty();

		var mailRecipient = mailResponse.getString("[0].headers.to");
		assertThat(mailRecipient).as("O destinatário do e-mail deve ser o cliente: %s", clientName)
				.isEqualTo(clientMail);
	}

	/**
	 * Verifica se o e-mail enviado contém o nome do cliente.
	 */
	@Entao("o email deve conter o nome do cliente")
	public void oEmailDeveConterONomeDoCliente() {
		var body = mailResponse.getString("[0].html");
		assertThat(body).as("O e-mail deve conter o nome do cliente: %s", clientName).contains(clientName);
	}

	/**
	 * Verifica se o e-mail enviado contém o número do pedido.
	 */
	@Entao("o email deve conter o número do pedido")
	public void oEmailDeveConterONumeroDoPedido() {
		var body = mailResponse.getString("[0].html");
		assertThat(body).as("O e-mail deve conter o número do pedido: %s", orderReadyEventDto.getOrderNumber())
				.contains(orderReadyEventDto.getOrderNumber());
	}

	/**
	 * Verifica se o e-mail enviado contém o momento em que o pedido ficou pronto.
	 */
	@Entao("o email deve conter o momento em que o pedido ficou pronto")
	public void oEmailDeveConterOMomentoEmQueOPedidoFicouPronto() {
		var body = mailResponse.getString("[0].html");
		var readyAtFormatted = orderReadyEventDto.getReadyAt();

		assertThat(body).as("O e-mail deve conter o momento em que o pedido ficou pronto: %s", readyAtFormatted)
				.contains(readyAtFormatted);
	}
}
