package com.soat.fiap.food.core.order.infrastructure.common.source;

import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderCanceledEventDto;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderReadyEventDto;

/**
 * Interface para publicação de eventos no sistema.
 * <p>
 * Fornece métodos para publicar diferentes tipos de eventos. Implementações
 * dessa interface podem enviar os eventos para mecanismos de mensageria (como
 * RabbitMQ) ou outros sistemas de eventos.
 * </p>
 */
public interface EventPublisherSource {

	/**
	 * Publica um evento de pedido criado.
	 *
	 * @param orderCreatedEventDto
	 *            evento contendo informações do pedido criado.
	 */
	void publishOrderCreatedEvent(OrderCreatedEventDto orderCreatedEventDto);

	/**
	 * Publica um evento de pedido cancelado.
	 *
	 * @param orderCanceledEventDto
	 *            evento contendo informações do pedido cancelado.
	 */
	void publishOrderCanceledEvent(OrderCanceledEventDto orderCanceledEventDto);

	/**
	 * Publica um evento de pedido quando ele está pronto.
	 *
	 * @param orderReadyEventDto
	 *            evento contendo informações do pedido quando ele está pronto.
	 */
	void publishOrderReadyEvent(OrderReadyEventDto orderReadyEventDto);

}
