package com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.catalog.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soat.fiap.food.core.order.core.interfaceadapters.bff.controller.web.api.ChargebackOrderController;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.StockReversalEventDto;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.PaymentDataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler responsável por processar eventos de estorno de estoque.
 *
 * <p>
 * Quando um evento {@link StockReversalEventDto} é recebido, este handler
 * executa o fluxo de estorno do pedido, atualizando o status.
 * </p>
 */
@Slf4j @Service @RequiredArgsConstructor
public class StockReversalHandler {

	private final OrderDataSource orderDataSource;
	private final PaymentDataSource paymentDataSource;

	/**
	 * Processa o evento de estorno de estoque.
	 *
	 * @param event
	 *            evento de estorno de estoque recebido
	 */
	@Transactional
	public void handle(StockReversalEventDto event) {
		log.info("Evento de estorno de estoque recebido: {}", event.getOrderId());

		ChargebackOrderController.chargebackOrder(event.getOrderId(), orderDataSource, paymentDataSource);

		log.info("Status do pedido atualizado após estorno de estoque: {}", event.getOrderId());
	}
}
