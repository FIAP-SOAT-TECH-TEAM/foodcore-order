package com.soat.fiap.food.core.order.infrastructure.out.event.publisher.azsvcbus;
import org.springframework.stereotype.Component;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.google.gson.Gson;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderCanceledEventDto;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderReadyEventDto;
import com.soat.fiap.food.core.order.infrastructure.common.source.EventPublisherSource;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementação do {@link EventPublisherSource} usando Azure Service Bus.
 * <p>
 * Esta classe envia eventos de domínio para tópicos e filas do Azure Service
 * Bus correspondentes. Cada método publica um tipo de evento específico.
 * </p>
 */
@Slf4j @Component @AllArgsConstructor
public class AzSvcBusEventPublisher implements EventPublisherSource {

	private final ServiceBusSenderClient orderCreatedSender;
	private final ServiceBusSenderClient orderCanceledSender;
	private final ServiceBusSenderClient orderReadySender;
	private final Gson gson;

	/**
	 * Publica um evento de pedido criado no tópico correspondente do Azure Service
	 * Bus.
	 *
	 * @param event
	 *            Evento de pedido criado
	 */
	@Override
	public void publishOrderCreatedEvent(OrderCreatedEventDto event) {
		try {
			orderCreatedSender.sendMessage(new ServiceBusMessage(gson.toJson(event)));
			log.info("Evento de pedido criado publicado com sucesso: {}", event);
		} catch (Exception ex) {
			log.error("Erro ao publicar evento de pedido criado", ex);
		}
	}

	/**
	 * Publica um evento de pedido cancelado na fila correspondente do Azure Service
	 * Bus.
	 *
	 * @param event
	 *            Evento de pedido cancelado
	 */
	@Override
	public void publishOrderCanceledEvent(OrderCanceledEventDto event) {
		try {
			orderCanceledSender.sendMessage(new ServiceBusMessage(gson.toJson(event)));
			log.info("Evento de pedido cancelado publicado com sucesso: {}", event);
		} catch (Exception ex) {
			log.error("Erro ao publicar evento de pedido cancelado", ex);
		}
	}

	/**
	 * Publica um evento de pedido pronto na fila correspondente do Azure Service
	 * Bus.
	 *
	 * @param event
	 *            Evento de pedido pronto
	 */
	@Override
	public void publishOrderReadyEvent(OrderReadyEventDto event) {
		try {
			orderReadySender.sendMessage(new ServiceBusMessage(gson.toJson(event)));
			log.info("Evento de pedido pronto publicado com sucesso: {}", event);
		} catch (Exception ex) {
			log.error("Erro ao publicar evento de pedido pronto", ex);
		}
	}
}
