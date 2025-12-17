package com.soat.fiap.food.core.order.core.interfaceadapters.bff.controller.web.api;

import com.soat.fiap.food.core.order.core.application.usecases.*;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderAlreadyHasStatusException;
import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.bff.presenter.web.api.OrderPresenter;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.EventPublisherGateway;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.OrderGateway;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.PaymentGateway;
import com.soat.fiap.food.core.order.infrastructure.common.source.EventPublisherSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.PaymentDataSource;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.OrderStatusRequest;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.response.OrderStatusResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller: Atualizar status do pedido.
 */
@Slf4j
public class UpdateOrderStatusController {

	/**
	 * Atualizaa status do pedido.
	 *
	 * @param id
	 *            ID do pedido
	 * @param orderStatusRequest
	 *            Status do pedido a ser atualizado
	 * @param orderDataSource
	 *            Origem de dados para o gateway de pedido
	 * @param paymentDataSource
	 *            Origem de dados para o gateway de pagamento
	 * @param eventPublisherSource
	 *            Origem de publicação de eventos
	 * @return Pedido atualizado
	 */
	public static OrderStatusResponse updateOrderStatus(Long id, OrderStatusRequest orderStatusRequest,
			OrderDataSource orderDataSource, PaymentDataSource paymentDataSource,
			EventPublisherSource eventPublisherSource) {

		var orderGateway = new OrderGateway(orderDataSource);
		var paymentGateway = new PaymentGateway(paymentDataSource);
		var eventPublisherGateway = new EventPublisherGateway(eventPublisherSource);

		try {

			EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(id, orderStatusRequest.getStatus(),
					paymentGateway, orderGateway);

			log.info("Atualizando status do pedido {} para {}", id, orderStatusRequest.getStatus());

			var order = UpdateOrderStatusUseCase.updateOrderStatus(id, orderStatusRequest.getStatus(), orderGateway);
			var updatedOrder = orderGateway.save(order);

			log.info("Status do pedido {} atualizado para {}", id, updatedOrder);

			if (updatedOrder.getOrderStatus() == OrderStatus.CANCELLED) {
				PublishOrderCanceledEventUseCase.publishOrderCanceledEvent(updatedOrder, eventPublisherGateway);
			} else if (updatedOrder.getOrderStatus() == OrderStatus.READY) {
				PublishOrderReadyEventUseCase.publishCreateOrderEvent(updatedOrder, eventPublisherGateway);
			}

			return OrderPresenter.toOrderStatusResponse(updatedOrder);
		} catch (OrderAlreadyHasStatusException ex) {
			var existingOrder = GetOrderByIdUseCase.getOrderById(id, orderGateway);
			return OrderPresenter.toOrderStatusResponse(existingOrder);
		}
	}
}
