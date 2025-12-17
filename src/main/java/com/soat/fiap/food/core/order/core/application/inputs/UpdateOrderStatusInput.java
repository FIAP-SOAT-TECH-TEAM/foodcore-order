package com.soat.fiap.food.core.order.core.application.inputs;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;

/**
 * Representa um DTO de entrada da aplicação (Application Layer) para
 * atualização do status de um pedido.
 */
public record UpdateOrderStatusInput(OrderStatus status) {

	/**
	 * Construtor do record {@code UpdateOrderStatusInput}.
	 *
	 * @param status
	 *            Novo status do pedido.
	 */
	public UpdateOrderStatusInput {
	}
}
