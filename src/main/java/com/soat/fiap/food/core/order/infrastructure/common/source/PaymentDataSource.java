package com.soat.fiap.food.core.order.infrastructure.common.source;

import com.soat.fiap.food.core.order.core.interfaceadapters.dto.payment.PaymentStatusDTO;

/**
 * DataSource para obtenção de Pagamento.
 */
public interface PaymentDataSource {

	/**
	 * Retorna o status de pagamento de um pedido pelo seu ID
	 *
	 * @param orderId
	 *            ID do pedido
	 */
	PaymentStatusDTO getOrderStatus(Long orderId);
}
