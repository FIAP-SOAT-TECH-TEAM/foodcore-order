package com.soat.fiap.food.core.order.core.application.inputs;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;

/**
 * Representa um DTO de entrada da aplicação (Application Layer) contendo o novo
 * status do pedido.
 */
public record OrderStatusInput(OrderStatus orderStatus) {

	/**
	 * Construtor do record {@code OrderStatusInput}.
	 *
	 * @param orderStatus
	 *            Novo status do pedido.
	 */
	public OrderStatusInput {
	}
}
