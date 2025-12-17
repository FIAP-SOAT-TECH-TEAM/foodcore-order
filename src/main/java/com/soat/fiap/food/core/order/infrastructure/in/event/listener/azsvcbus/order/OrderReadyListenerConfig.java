package com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.google.gson.Gson;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderReadyEventDto;
import com.soat.fiap.food.core.order.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.order.handlers.OrderReadyHandler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Listener responsável por processar eventos de pedido pronto.
 *
 * <p>
 * Dispara email para o cliente, notificando-o de que o pedido está pronto
 * </p>
 */
@Configuration @Slf4j @RequiredArgsConstructor @Transactional
public class OrderReadyListenerConfig {

	private final Gson gson;
	private final OrderReadyHandler orderReadyHandler;

	@Bean
	public ServiceBusProcessorClient orderReadyServiceBusProcessorClient(ServiceBusClientBuilder builder) {

		return builder.processor()
				.queueName(ServiceBusConfig.ORDER_READY_QUEUE)
				.receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
				.processMessage(context -> {
					OrderReadyEventDto event = gson.fromJson(context.getMessage().getBody().toString(),
							OrderReadyEventDto.class);
					orderReadyHandler.handle(event);
				})
				.processError(context -> log.error("Erro ao processar evento de pedido pronto", context.getException()))
				.buildProcessorClient();
	}
}
