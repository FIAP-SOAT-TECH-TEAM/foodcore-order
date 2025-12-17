package com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.catalog;

import org.springframework.stereotype.Component;

import com.azure.messaging.servicebus.ServiceBusProcessorClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * Inicializa os processadores de eventos relacionados a estoque.
 *
 * <p>
 * Garante que todos os listeners de estoque sejam iniciados automaticamente
 * após o carregamento do contexto da aplicação.
 * </p>
 */
@RequiredArgsConstructor @Component
public class StockListener {

	private final ServiceBusProcessorClient stockReversalServiceBusProcessorClient;
	private final ServiceBusProcessorClient stockDebitErrorServiceBusProcessorClient;

	@PostConstruct
	public void run() {
		stockReversalServiceBusProcessorClient.start();
		stockDebitErrorServiceBusProcessorClient.start();
	}
}
