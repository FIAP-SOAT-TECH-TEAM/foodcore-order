package com.soat.fiap.food.core.order.core.interfaceadapters.bff.presenter.web.api;

import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.response.OrderStatusResponse;

/**
 * Presenter responsável por converter objetos do domínio {@link Order} em
 * objetos de resposta {@link OrderStatusResponse} utilizados especificamente na
 * camada de API web (web.api).
 */
public class OrderStatusPresenter {

	/**
	 * Converte a entidade {@link Order} para um {@link OrderStatusResponse}.
	 *
	 * @param order
	 *            A entidade {@link Order} com o status atualizado.
	 * @return Um DTO {@link OrderStatusResponse} contendo o ID e o novo status.
	 */
	public static OrderStatusResponse toOrderStatusResponse(Order order) {
		return new OrderStatusResponse(order.getId(), order.getOrderStatus(), order.getCreatedAt(),
				order.getUpdatedAt());
	}
}
