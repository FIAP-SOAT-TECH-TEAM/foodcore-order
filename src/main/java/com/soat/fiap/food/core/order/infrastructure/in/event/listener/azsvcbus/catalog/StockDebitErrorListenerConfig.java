package com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.catalog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.SubQueue;
import com.google.gson.Gson;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.order.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.catalog.handlers.StockDebitErrorHandler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Listener responsável por processar eventos de erro no débito de estoque.
 *
 * @see <a href=
 *      "https://learn.microsoft.com/pt-br/java/api/overview/azure/messaging-servicebus-readme?view=azure-java-stable#create-a-dead-letter-queue-receiver">Create
 *      a dead-letter queue Receiver - Azure Service Bus</a>
 */
@Configuration @Slf4j @RequiredArgsConstructor @Transactional
public class StockDebitErrorListenerConfig {

	private final Gson gson;
	private final StockDebitErrorHandler stockDebitErrorHandler;

	@Bean
	public ServiceBusProcessorClient stockDebitErrorServiceBusProcessorClient(ServiceBusClientBuilder builder) {

		return builder.processor()
				.topicName(ServiceBusConfig.ORDER_CREATED_TOPIC)
				.subscriptionName(ServiceBusConfig.CATALOG_ORDER_CREATED_TOPIC_SUBSCRIPTION)
				.subQueue(SubQueue.DEAD_LETTER_QUEUE)
				.processMessage(context -> {
					OrderCreatedEventDto event = gson.fromJson(context.getMessage().getBody().toString(),
							OrderCreatedEventDto.class);
					stockDebitErrorHandler.handle(event);
				})
				.processError(context -> log.error("Erro ao processar evento de erro no débito de estoque",
						context.getException()))
				.buildProcessorClient();
	}
}
