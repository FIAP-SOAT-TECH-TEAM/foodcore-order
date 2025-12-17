package com.soat.fiap.food.core.order.core.application.usecases;

import com.soat.fiap.food.core.order.core.application.inputs.mappers.OrderCreatedEventMapper;
import com.soat.fiap.food.core.order.core.domain.events.OrderCreatedEvent;
import com.soat.fiap.food.core.order.core.domain.events.OrderItemCreatedEvent;
import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.EventPublisherGateway;

import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso: publicar o evento {@link OrderCreatedEvent}
 */
@Slf4j
public class PublishOrderCreatedEventUseCase {

	/**
	 * Publica o evento {@link OrderCreatedEvent}, incluindo os itens do pedido como
	 * {@link OrderItemCreatedEvent}.
	 *
	 * @param order
	 *            O pedido criado que será convertido em evento.
	 * @param gateway
	 *            O gateway responsável por publicar o evento.
	 */
	public static void publishCreateOrderEvent(Order order, EventPublisherGateway gateway) {
		var event = OrderCreatedEventMapper.toEvent(order);

		log.info("Publicando evento de pedido criado {}", order.getId());

		gateway.publishOrderCreatedEvent(event);
	}
}
