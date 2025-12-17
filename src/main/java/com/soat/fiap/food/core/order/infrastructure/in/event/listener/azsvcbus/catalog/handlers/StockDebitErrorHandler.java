package com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.catalog.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soat.fiap.food.core.order.core.interfaceadapters.bff.controller.web.api.ChargebackOrderController;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.PaymentDataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler responsável por processar eventos de erro no débito de estoque.
 *
 * <p>
 * Quando ocorre falha no débito de estoque, este handler executa o fluxo de
 * estorno do pedido, atualizando o status.
 * </p>
 */
@Slf4j @Service @RequiredArgsConstructor
public class StockDebitErrorHandler {

	private final OrderDataSource orderDataSource;
	private final PaymentDataSource paymentDataSource;

	/**
	 * Processa o evento de erro no débito de estoque.
	 *
	 * @param event
	 *            evento de criação do pedido
	 */
	@Transactional
	public void handle(OrderCreatedEventDto event) {
		log.info("Evento de erro no débito de estoque recebido: {}", event.id);

		ChargebackOrderController.chargebackOrder(event.id, orderDataSource, paymentDataSource);

		log.info("Status do pedido atualizado após erro no débito de estoque: {}", event.id);
	}
}
