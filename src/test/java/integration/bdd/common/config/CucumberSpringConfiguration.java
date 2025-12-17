package integration.bdd.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.soat.fiap.food.core.order.FoodCoreApiApplication;

import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;

/**
 * Configuração de integração entre o Cucumber e o contexto do Spring Boot para
 * execução de testes BDD.
 * <p>
 * Esta classe inicializa o contexto da aplicação {@link FoodCoreApiApplication}
 * com o perfil de testes ativo e integra o suporte ao {@code Testcontainers}
 * por meio da herança de {@link TestContainersConfiguration}.
 * </p>
 *
 * <p>
 * <b>Uso:</b>
 * </p>
 * <ul>
 * <li>Permite executar cenários BDD que utilizam o contexto real do Spring
 * Boot.</li>
 * <li>Carrega o perfil {@code test} para isolamento de ambiente.</li>
 * </ul>
 *
 * @see TestContainersConfiguration
 * @see CucumberContextConfiguration
 * @see SpringBootTest
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FoodCoreApiApplication.class)
@ActiveProfiles("test") @CucumberContextConfiguration
public class CucumberSpringConfiguration extends TestContainersConfiguration {
	@LocalServerPort
	protected int localServerPort;

	@Value("${server.servlet.context-path}")
	protected String contextPath;

	@PostConstruct
	public void initRestAssured() {
		RestAssured.basePath = String.format("%s/orders", contextPath);
		RestAssured.port = localServerPort;
	}

	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) {
		registry.add("aws.cognito.userPoolId", () -> cognitoUserPoolId);
		registry.add("aws.cognito.endpoint", () -> cognitoEndpoint);
		registry.add("azsvcbus.connection-string", () -> azSvcBusConnectionString);
		registry.add("spring.mail.port", () -> mailDevSmtpHostPort);
	}

}
