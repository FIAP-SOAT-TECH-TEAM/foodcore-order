package com.soat.fiap.food.core.order.core.interfaceadapters.gateways;

import com.soat.fiap.food.core.order.core.domain.events.OrderCanceledEvent;
import com.soat.fiap.food.core.order.core.domain.events.OrderCreatedEvent;
import com.soat.fiap.food.core.order.core.domain.events.OrderReadyEvent;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.mappers.OrderCanceledEventMapper;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.mappers.OrderCreatedEventMapper;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.mappers.OrderReadyEventMapper;
import com.soat.fiap.food.core.order.infrastructure.common.source.EventPublisherSource;

/**
 * Gateway para publicação de eventos de domínio.
 * <p>
 * Este gateway delega a publicação de eventos ao {@link EventPublisherSource},
 * fornecendo métodos específicos para cada tipo de evento de domínio.
 * </p>
 */
public class EventPublisherGateway {

	private final EventPublisherSource eventPublisherSource;

	public EventPublisherGateway(EventPublisherSource eventPublisherSource) {
		this.eventPublisherSource = eventPublisherSource;
	}

	/**
	 * Publica um evento de pedido criado.
	 *
	 * @param event
	 *            Evento contendo informações do pedido criado.
	 */
	public void publishOrderCreatedEvent(OrderCreatedEvent event) {
		var eventDto = OrderCreatedEventMapper.toDto(event);

		eventPublisherSource.publishOrderCreatedEvent(eventDto);
	}

	/**
	 * Publica um evento de pedido cancelado.
	 *
	 * @param event
	 *            Evento contendo informações do pedido cancelado.
	 */
	public void publishOrderCanceledEvent(OrderCanceledEvent event) {
		var eventDto = OrderCanceledEventMapper.toDto(event);

		eventPublisherSource.publishOrderCanceledEvent(eventDto);
	}

	/**
	 * Publica um evento de pedido pronto.
	 *
	 * @param event
	 *            Evento contendo informações do pedido pronto.
	 */
	public void publishOrderReadyEvent(OrderReadyEvent event) {
		var eventDto = OrderReadyEventMapper.toDto(event);

		eventPublisherSource.publishOrderReadyEvent(eventDto);
	}
}
