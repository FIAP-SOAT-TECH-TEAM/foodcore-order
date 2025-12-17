package com.soat.fiap.food.core.order.core.application.inputs.mappers;

import com.soat.fiap.food.core.order.core.application.inputs.UpdateOrderStatusInput;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.OrderStatusRequest;

/**
 * Classe utilitária responsável por mapear objetos entre
 * {@link OrderStatusRequest} e {@link UpdateOrderStatusInput}.
 */
public class UpdateOrderStatusMapper {

	/**
	 * Converte um {@link OrderStatusRequest} (camada web.api) em um
	 * {@link UpdateOrderStatusInput} (camada de aplicação).
	 *
	 * @param request
	 *            O DTO de atualização de status recebido via API.
	 * @return Um objeto {@link UpdateOrderStatusInput} com o novo status.
	 */
	public static UpdateOrderStatusInput toInput(OrderStatusRequest request) {
		return new UpdateOrderStatusInput(request.getStatus());
	}
}
