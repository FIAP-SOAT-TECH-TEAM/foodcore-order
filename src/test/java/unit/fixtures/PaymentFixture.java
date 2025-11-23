package unit.fixtures;

import com.soat.fiap.food.core.order.core.interfaceadapters.dto.payment.PaymentStatusDTO;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.payment.StatusDTO;

/**
 * Fixture utilitária para criação de objetos relacionados a pagamentos
 * utilizados em testes unitários do módulo de pedidos.
 * <p>
 * Fornece métodos para gerar diferentes estados de pagamento de forma simples e
 * padronizada, facilitando a construção de cenários de teste.
 */
public class PaymentFixture {

	/**
	 * Cria um status de pagamento marcado como aprovado.
	 *
	 * @param orderId
	 *            ID do pedido associado ao pagamento
	 * @return instância de {@link PaymentStatusDTO} com status
	 *         {@link StatusDTO#APPROVED}
	 */
	public static PaymentStatusDTO createApprovedPaymentStatus(Long orderId) {
		return new PaymentStatusDTO(orderId, StatusDTO.APPROVED);
	}

	/**
	 * Cria um status de pagamento pendente.
	 * <p>
	 * Utilizado para simular pagamentos ainda não confirmados ou autorizados.
	 *
	 * @param orderId
	 *            ID do pedido associado ao pagamento
	 * @return instância de {@link PaymentStatusDTO} com status
	 *         {@link StatusDTO#PENDING}
	 */
	public static PaymentStatusDTO createPendingPaymentStatus(Long orderId) {
		return new PaymentStatusDTO(orderId, StatusDTO.PENDING);
	}
}
