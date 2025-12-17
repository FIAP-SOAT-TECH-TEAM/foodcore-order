package com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.order;

import org.springframework.stereotype.Component;

import com.azure.messaging.servicebus.ServiceBusProcessorClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * Inicializa os processadores de eventos relacionados a pedido.
 *
 * <p>
 * Garante que todos os listeners de pedido sejam iniciados automaticamente após
 * o carregamento do contexto da aplicação.
 * </p>
 */
@RequiredArgsConstructor @Component
public class OrderListener {

	private final ServiceBusProcessorClient orderReadyServiceBusProcessorClient;

	@PostConstruct
	public void run() {
		orderReadyServiceBusProcessorClient.start();
	}
}
