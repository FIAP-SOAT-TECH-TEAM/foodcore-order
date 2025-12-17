package com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.payment;

import org.springframework.stereotype.Component;

import com.azure.messaging.servicebus.ServiceBusProcessorClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * Inicializa os processadores de eventos relacionados a pagamentos.
 *
 * <p>
 * Garante que todos os listeners de pagamento sejam iniciados automaticamente
 * após o carregamento do contexto da aplicação.
 * </p>
 */
@RequiredArgsConstructor @Component
public class PaymentListener {

	private final ServiceBusProcessorClient paymentApprovedServiceBusProcessorClient;
	private final ServiceBusProcessorClient paymentExpiredServiceBusProcessorClient;

	@PostConstruct
	public void run() {
		paymentApprovedServiceBusProcessorClient.start();
		paymentExpiredServiceBusProcessorClient.start();
	}
}
