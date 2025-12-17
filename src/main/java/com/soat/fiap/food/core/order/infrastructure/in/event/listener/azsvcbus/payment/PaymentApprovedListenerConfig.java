package com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.payment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.google.gson.Gson;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.PaymentApprovedEventDto;
import com.soat.fiap.food.core.order.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.payment.handlers.PaymentApprovedHandler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Listener responsável por processar eventos de pagamento aprovado.
 *
 * <p>
 * Este listener trata o evento recebido e executa o tratamento necessário para
 * atualização do pedido e publicação de eventos relacionados.
 * </p>
 */
@Configuration @Slf4j @RequiredArgsConstructor @Transactional
public class PaymentApprovedListenerConfig {

	private final Gson gson;
	private final PaymentApprovedHandler paymentApprovedHandler;

	@Bean
	public ServiceBusProcessorClient paymentApprovedServiceBusProcessorClient(ServiceBusClientBuilder builder) {

		return builder.processor()
				.queueName(ServiceBusConfig.PAYMENT_APPROVED_QUEUE)
				.receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
				.processMessage(context -> {
					PaymentApprovedEventDto event = gson.fromJson(context.getMessage().getBody().toString(),
							PaymentApprovedEventDto.class);
					paymentApprovedHandler.handle(event);
				})
				.processError(context -> log.error("Erro ao processar pagamento aprovado", context.getException()))
				.buildProcessorClient();
	}
}
