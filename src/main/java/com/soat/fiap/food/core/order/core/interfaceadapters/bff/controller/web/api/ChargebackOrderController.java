package com.soat.fiap.food.core.order.core.interfaceadapters.bff.controller.web.api;

import com.soat.fiap.food.core.order.core.application.usecases.EnsureOrderPaymentIsValidUseCase;
import com.soat.fiap.food.core.order.core.application.usecases.UpdateOrderStatusUseCase;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderAlreadyHasStatusException;
import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.OrderGateway;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.PaymentGateway;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.PaymentDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller: Estornar pedido.
 */
@Slf4j
public class ChargebackOrderController {

	/**
	 * Estorna pedido.
	 *
	 * @param id
	 *            ID do pedido
	 * @param orderDataSource
	 *            Origem de dados para o gateway de pedido
	 * @param paymentDataSource
	 *            Origem de dados para o gateway de pagamento
	 */
	public static void chargebackOrder(Long id, OrderDataSource orderDataSource, PaymentDataSource paymentDataSource) {

		var orderGateway = new OrderGateway(orderDataSource);
		var paymentGateway = new PaymentGateway(paymentDataSource);

		try {

			var chargebackOrderStatus = OrderStatus.CANCELLED;

			EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(id, chargebackOrderStatus, paymentGateway,
					orderGateway);

			log.info("Atualizando status do pedido {} para {}", id, chargebackOrderStatus);

			var order = UpdateOrderStatusUseCase.updateOrderStatus(id, chargebackOrderStatus, orderGateway);
			var updatedOrder = orderGateway.save(order);

			log.info("Status do pedido {} atualizado para {}", id, updatedOrder);
		} catch (OrderAlreadyHasStatusException ex) {
			log.info("Pedido já estornado: {}", id);
		}
	}
}
