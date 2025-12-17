package com.soat.fiap.food.core.order.core.interfaceadapters.gateways;

import com.soat.fiap.food.core.order.core.interfaceadapters.dto.payment.PaymentStatusDTO;
import com.soat.fiap.food.core.order.infrastructure.common.source.PaymentDataSource;

/**
 * Gateway para comunicação com o microsserviço de Pagamento
 */
public class PaymentGateway {

	private final PaymentDataSource paymentDataSource;

	public PaymentGateway(PaymentDataSource paymentDataSource) {
		this.paymentDataSource = paymentDataSource;
	}

	/**
	 * Retorna o status de pagamento de um pedido pelo seu ID
	 *
	 * @param orderId
	 *            ID do pedido
	 */
	public PaymentStatusDTO getOrderStatus(Long orderId) {
		return paymentDataSource.getOrderStatus(orderId);
	}

}
