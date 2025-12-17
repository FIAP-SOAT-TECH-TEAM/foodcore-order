package com.soat.fiap.food.core.order.core.interfaceadapters.bff.presenter.web.api;

import java.util.List;

import com.soat.fiap.food.core.order.core.domain.model.OrderItem;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.response.OrderItemResponse;

/**
 * Presenter responsável por converter objetos do domínio {@link OrderItem} em
 * objetos de resposta {@link OrderItemResponse} utilizados na camada de API web
 * (web.api).
 */
public class OrderItemPresenter {

	/**
	 * Converte uma instância da entidade {@link OrderItem} para um
	 * {@link OrderItemResponse}.
	 *
	 * @param item
	 *            A entidade de domínio {@link OrderItem} a ser convertida.
	 * @return Um DTO {@link OrderItemResponse} com os dados formatados para
	 *         resposta HTTP.
	 */
	public static OrderItemResponse toOrderItemResponse(OrderItem item) {
		return new OrderItemResponse(item.getId(), item.getProductId(), item.getName(), item.getQuantity(),
				item.getUnitPrice(), item.getSubTotal(), item.getObservations(), item.getCreatedAt(),
				item.getUpdatedAt());
	}

	/**
	 * Converte uma lista de instâncias da entidade {@link OrderItem} para uma lista
	 * de {@link OrderItemResponse}.
	 *
	 * @param items
	 *            A lista de entidades {@link OrderItem} a serem convertidas.
	 * @return Uma lista de DTOs {@link OrderItemResponse} com os dados formatados
	 *         para resposta HTTP.
	 */
	public static List<OrderItemResponse> toListOrderItemResponse(List<OrderItem> items) {
		return items.stream().map(OrderItemPresenter::toOrderItemResponse).toList();
	}
}
