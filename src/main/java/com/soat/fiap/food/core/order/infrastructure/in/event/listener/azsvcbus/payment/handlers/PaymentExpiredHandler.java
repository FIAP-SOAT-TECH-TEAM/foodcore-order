package com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.payment.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.bff.controller.web.api.UpdateOrderStatusController;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.PaymentExpiredEventDto;
import com.soat.fiap.food.core.order.infrastructure.common.source.EventPublisherSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.PaymentDataSource;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.OrderStatusRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler responsável por processar eventos de pagamento expirado.
 *
 * <p>
 * Quando um evento {@link PaymentExpiredEventDto} é recebido, este handler
 * atualiza o status do pedido para {@code CANCELLED} e executa as ações
 * necessárias relacionadas ao cancelamento do pedido.
 * </p>
 */
@Slf4j @Service @RequiredArgsConstructor
public class PaymentExpiredHandler {

	private final OrderDataSource orderDataSource;
	private final PaymentDataSource paymentDataSource;
	private final EventPublisherSource eventPublisherSource;

	/**
	 * Processa o evento de pagamento expirado.
	 *
	 * @param event
	 *            evento de pagamento expirado recebido
	 */
	@Transactional
	public void handle(PaymentExpiredEventDto event) {
		log.info("Evento de pagamento expirado recebido: {}", event.getOrderId());

		var orderUpdateStatusRequest = new OrderStatusRequest(OrderStatus.CANCELLED);
		UpdateOrderStatusController.updateOrderStatus(event.getOrderId(), orderUpdateStatusRequest, orderDataSource,
				paymentDataSource, eventPublisherSource);

		log.info("Status do pedido atualizado após pagamento expirado: {}", event.getOrderId());
	}
}
