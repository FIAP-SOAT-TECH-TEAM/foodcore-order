package com.soat.fiap.food.core.order.core.application.usecases;

import com.soat.fiap.food.core.order.core.application.inputs.mappers.OrderCanceledEventMapper;
import com.soat.fiap.food.core.order.core.domain.events.OrderCanceledEvent;
import com.soat.fiap.food.core.order.core.domain.events.OrderItemCanceledEvent;
import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.EventPublisherGateway;

import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso: publicar o evento {@link OrderCanceledEvent} com os itens do
 * pedido como {@link OrderItemCanceledEvent}.
 */
@Slf4j
public class PublishOrderCanceledEventUseCase {

	/**
	 * Publica o evento {@link OrderCanceledEvent}, incluindo os itens do pedido
	 * como eventos do tipo {@link OrderItemCanceledEvent}.
	 *
	 * @param order
	 *            O pedido cancelado que será convertido em evento.
	 * @param gateway
	 *            O gateway responsável por publicar o evento.
	 */
	public static void publishOrderCanceledEvent(Order order, EventPublisherGateway gateway) {
		var event = OrderCanceledEventMapper.toEvent(order);

		log.info("Publicando evento de pedido cancelado {}", order.getId());

		gateway.publishOrderCanceledEvent(event);
	}
}
