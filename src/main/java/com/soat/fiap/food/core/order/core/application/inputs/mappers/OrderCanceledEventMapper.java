package com.soat.fiap.food.core.order.core.application.inputs.mappers;

import java.util.List;

import com.soat.fiap.food.core.order.core.domain.events.OrderCanceledEvent;
import com.soat.fiap.food.core.order.core.domain.events.OrderItemCanceledEvent;
import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.core.domain.model.OrderItem;

/**
 * Classe utilitária responsável por mapear uma entidade de domínio
 * {@link Order} para o evento {@link OrderCanceledEvent}, incluindo os itens do
 * pedido.
 */
public class OrderCanceledEventMapper {

	/**
	 * Converte uma entidade de domínio {@link Order} em um evento
	 * {@link OrderCanceledEvent}, mapeando seus atributos e os itens cancelados
	 * para eventos {@link OrderItemCanceledEvent}.
	 *
	 * @param order
	 *            A entidade {@link Order} que foi cancelada.
	 * @return Um evento {@link OrderCanceledEvent} com os dados do pedido
	 *         cancelado.
	 */
	public static OrderCanceledEvent toEvent(Order order) {
		OrderCanceledEvent event = new OrderCanceledEvent();
		event.setId(order.getId());
		event.setStatus(order.getOrderStatus());

		List<OrderItemCanceledEvent> itemEvents = order.getOrderItems()
				.stream()
				.map(OrderCanceledEventMapper::toItemEvent)
				.toList();

		event.setItems(itemEvents);

		return event;
	}

	/**
	 * Converte um item {@link OrderItem} em um evento
	 * {@link OrderItemCanceledEvent}.
	 *
	 * @param item
	 *            O item do pedido que foi cancelado.
	 * @return Um evento {@link OrderItemCanceledEvent} com os dados do item
	 *         cancelado.
	 */
	private static OrderItemCanceledEvent toItemEvent(OrderItem item) {
		OrderItemCanceledEvent itemEvent = new OrderItemCanceledEvent();
		itemEvent.setId(item.getId());
		itemEvent.setProductId(item.getProductId());
		itemEvent.setName(item.getName());
		itemEvent.setQuantity(item.getQuantity());
		itemEvent.setUnitPrice(item.getUnitPrice());
		itemEvent.setSubtotal(item.getSubTotal());
		itemEvent.setObservations(item.getObservations());
		return itemEvent;
	}
}
