package com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.catalog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.google.gson.Gson;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.StockReversalEventDto;
import com.soat.fiap.food.core.order.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.catalog.handlers.StockReversalHandler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Listener responsável por processar eventos de estorno de estoque.
 *
 * <p>
 * Atualiza o status do pedido e executa o tratamento necessário para eventos
 * relacionados.
 * </p>
 */
@Configuration @Slf4j @RequiredArgsConstructor @Transactional
public class StockReversaListenerConfig {

	private final Gson gson;
	private final StockReversalHandler stockReversalHandler;

	@Bean
	public ServiceBusProcessorClient stockReversalServiceBusProcessorClient(ServiceBusClientBuilder builder) {

		return builder.processor()
				.queueName(ServiceBusConfig.STOCK_REVERSAL_QUEUE)
				.receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
				.processMessage(context -> {
					StockReversalEventDto event = gson.fromJson(context.getMessage().getBody().toString(),
							StockReversalEventDto.class);
					stockReversalHandler.handle(event);
				})
				.processError(
						context -> log.error("Erro ao processar evento de estorno de estoque", context.getException()))
				.buildProcessorClient();
	}
}
