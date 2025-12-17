package com.soat.fiap.food.core.order.core.interfaceadapters.bff.presenter.web.api;

import java.util.List;

import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.response.OrderItemResponse;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.response.OrderResponse;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.response.OrderStatusResponse;

/**
 * Presenter responsável por converter objetos do domínio {@link Order} em
 * objetos de resposta {@link OrderResponse} utilizados na camada de API web
 * (web.api).
 */
public class OrderPresenter {

	/**
	 * Converte uma instância da entidade {@link Order} para um
	 * {@link OrderResponse}.
	 *
	 * @param order
	 *            A entidade de domínio {@link Order} a ser convertida.
	 * @return Um DTO {@link OrderResponse} com os dados formatados para resposta
	 *         HTTP.
	 */
	public static OrderResponse toOrderResponse(Order order) {
		List<OrderItemResponse> itemResponses = OrderItemPresenter.toListOrderItemResponse(order.getOrderItems());

		return new OrderResponse(order.getId(), order.getOrderNumber(), order.getOrderStatus(),
				order.getOrderStatus().getDescription(), order.getUserId(), order.getAmount(), itemResponses,
				order.getCreatedAt(), order.getUpdatedAt());
	}

	/**
	 * Converte uma instância da entidade {@link Order} para um
	 * {@link OrderStatusResponse}.
	 *
	 * @param order
	 *            A entidade de domínio {@link Order} a ser convertida.
	 * @return Um DTO {@link OrderStatusResponse} com os dados do status do pedido.
	 */
	public static OrderStatusResponse toOrderStatusResponse(Order order) {
		return OrderStatusResponse.builder()
				.orderId(order.getId())
				.orderStatus(order.getOrderStatus())
				.createdAt(order.getCreatedAt())
				.updatedAt(order.getUpdatedAt())
				.build();
	}

	/**
	 * Converte uma lista de instâncias da entidade {@link Order} para uma lista de
	 * {@link OrderResponse}, utilizada na exposição de dados via API REST
	 * (web.api).
	 *
	 * @param orders
	 *            A lista de entidades de domínio {@link Order} a serem convertidas.
	 * @return Uma lista de DTOs {@link OrderResponse} com os dados dos pedidos
	 *         formatados para resposta HTTP.
	 */
	public static List<OrderResponse> toListOrderResponse(List<Order> orders) {
		return orders.stream().map(OrderPresenter::toOrderResponse).toList();
	}
}
