package com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.payment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.google.gson.Gson;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.PaymentExpiredEventDto;
import com.soat.fiap.food.core.order.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.payment.handlers.PaymentExpiredHandler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Listener responsável por processar eventos de pagamento expirado.
 *
 * <p>
 * Atualiza o status do pedido e executa o tratamento necessário para eventos
 * relacionados.
 * </p>
 */
@Configuration @Slf4j @RequiredArgsConstructor @Transactional
public class PaymentExpiredListenerConfig {

	private final Gson gson;
	private final PaymentExpiredHandler paymentExpiredHandler;

	@Bean
	public ServiceBusProcessorClient paymentExpiredServiceBusProcessorClient(ServiceBusClientBuilder builder) {

		return builder.processor()
				.queueName(ServiceBusConfig.PAYMENT_EXPIRED_QUEUE)
				.receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
				.processMessage(context -> {
					PaymentExpiredEventDto event = gson.fromJson(context.getMessage().getBody().toString(),
							PaymentExpiredEventDto.class);
					paymentExpiredHandler.handle(event);
				})
				.processError(context -> log.error("Erro ao processar pagamento expirado", context.getException()))
				.buildProcessorClient();
	}
}
