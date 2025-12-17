package com.soat.fiap.food.core.order.core.application.inputs.mappers;

import java.util.List;

import com.soat.fiap.food.core.order.core.domain.events.OrderCreatedEvent;
import com.soat.fiap.food.core.order.core.domain.events.OrderItemCreatedEvent;
import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.core.domain.model.OrderItem;

/**
 * Classe utilitária responsável por mapear uma entidade de domínio
 * {@link Order} para o evento {@link OrderCreatedEvent}, incluindo os itens do
 * pedido.
 */
public class OrderCreatedEventMapper {

	/**
	 * Converte uma entidade de domínio {@link Order} em um evento
	 * {@link OrderCreatedEvent}, mapeando seus atributos e seus itens para eventos
	 * {@link OrderItemCreatedEvent}.
	 *
	 * @param order
	 *            A entidade {@link Order} a ser convertida.
	 * @return Um evento {@link OrderCreatedEvent} com os dados do pedido e seus
	 *         itens.
	 */
	public static OrderCreatedEvent toEvent(Order order) {
		OrderCreatedEvent event = new OrderCreatedEvent();
		event.setId(order.getId());
		event.setOrderNumber(order.getOrderNumber());
		event.setStatus(order.getOrderStatus());
		event.setStatusDescription(order.getOrderStatus().getDescription());
		event.setUserId(order.getUserId());
		event.setTotalAmount(order.getAmount());

		List<OrderItemCreatedEvent> itemEvents = order.getOrderItems()
				.stream()
				.map(OrderCreatedEventMapper::toItemEvent)
				.toList();

		event.setItems(itemEvents);

		return event;
	}

	/**
	 * Converte um item {@link OrderItem} em um evento
	 * {@link OrderItemCreatedEvent}.
	 *
	 * @param item
	 *            O item do pedido.
	 * @return Um evento {@link OrderItemCreatedEvent} com os dados do item.
	 */
	private static OrderItemCreatedEvent toItemEvent(OrderItem item) {
		OrderItemCreatedEvent itemEvent = new OrderItemCreatedEvent();
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
