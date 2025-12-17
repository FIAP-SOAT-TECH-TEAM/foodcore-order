package com.soat.fiap.food.core.order.core.application.usecases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.soat.fiap.food.core.order.core.application.inputs.mappers.OrderReadyEventMapper;
import com.soat.fiap.food.core.order.core.domain.events.OrderItemCreatedEvent;
import com.soat.fiap.food.core.order.core.domain.events.OrderReadyEvent;
import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.EventPublisherGateway;

import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso: publicar o evento {@link OrderReadyEvent}
 */
@Slf4j
public class PublishOrderReadyEventUseCase {

	/**
	 * Publica o evento {@link OrderReadyEvent}, incluindo os itens do pedido como
	 * {@link OrderItemCreatedEvent}.
	 *
	 * @param order
	 *            O pedido pronto que será convertido em evento.
	 * @param gateway
	 *            O gateway responsável por publicar o evento.
	 */
	public static void publishCreateOrderEvent(Order order, EventPublisherGateway gateway) {
		var event = OrderReadyEventMapper.toEvent(order);
		var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		event.setReadyAt(LocalDateTime.now().format(formatter));

		log.info("Publicando evento de pedido pronto {}", order.getId());

		gateway.publishOrderReadyEvent(event);
	}
}
