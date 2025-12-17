package com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.payment.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.bff.controller.web.api.UpdateOrderStatusController;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.PaymentApprovedEventDto;
import com.soat.fiap.food.core.order.infrastructure.common.source.EventPublisherSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.PaymentDataSource;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.OrderStatusRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler responsável por processar eventos de pagamento aprovado.
 *
 * <p>
 * Quando um evento {@link PaymentApprovedEventDto} é recebido, este handler
 * atualiza o status do pedido para {@code PREPARING} e publica eventos de
 * domínio relacionados à atualização de status.
 * </p>
 */
@Slf4j @Service @RequiredArgsConstructor
public class PaymentApprovedHandler {

	private final OrderDataSource orderDataSource;
	private final PaymentDataSource paymentDataSource;
	private final EventPublisherSource eventPublisherSource;

	/**
	 * Processa o evento de pagamento aprovado.
	 *
	 * @param event
	 *            evento de pagamento aprovado recebido
	 */
	@Transactional
	public void handle(PaymentApprovedEventDto event) {
		log.info("Evento de pagamento aprovado recebido: {}", event.getOrderId());

		var orderUpdateStatusRequest = new OrderStatusRequest(OrderStatus.PREPARING);
		UpdateOrderStatusController.updateOrderStatus(event.getOrderId(), orderUpdateStatusRequest, orderDataSource,
				paymentDataSource, eventPublisherSource);

		log.info("Status do pedido atualizado após pagamento aprovado: {}", event.getOrderId());
	}
}
