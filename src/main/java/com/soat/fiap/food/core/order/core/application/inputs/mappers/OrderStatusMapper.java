package com.soat.fiap.food.core.order.core.application.inputs.mappers;

import com.soat.fiap.food.core.order.core.application.inputs.OrderStatusInput;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.OrderStatusRequest;

/**
 * Classe utilitária responsável por mapear objetos entre
 * {@link OrderStatusRequest} e {@link OrderStatusInput}.
 */
public class OrderStatusMapper {

	/**
	 * Converte um {@link OrderStatusRequest} (camada web.api) em um
	 * {@link OrderStatusInput} (camada de aplicação).
	 *
	 * @param request
	 *            O DTO da requisição HTTP contendo o novo status.
	 * @return Um objeto {@link OrderStatusInput} contendo o novo status.
	 */
	public static OrderStatusInput toInput(OrderStatusRequest request) {
		return new OrderStatusInput(request.getStatus());
	}
}
