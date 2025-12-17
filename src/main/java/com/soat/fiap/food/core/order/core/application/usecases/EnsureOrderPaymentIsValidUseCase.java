package com.soat.fiap.food.core.order.core.application.usecases;

import com.soat.fiap.food.core.order.core.domain.exceptions.OrderNotFoundException;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderPaymentException;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderPaymentNotFoundException;
import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.payment.PaymentStatusDTO;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.payment.StatusDTO;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.OrderGateway;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.PaymentGateway;

import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso: Garantir que o pagamento do pedido seja válido.
 */
@Slf4j
public class EnsureOrderPaymentIsValidUseCase {

	/**
	 * Valida se o pagamento de um pedido é válido
	 *
	 * @param id
	 *            ID do Pedido a ser validado
	 * @param status
	 *            novo status do pedido
	 * @param paymentGateway
	 *            Gateway de pagamento para comunicação com o mundo exterior
	 * @param orderGateway
	 *            Gateway de pedido para comunicação com o mundo exterior
	 * @throws OrderPaymentNotFoundException
	 *             se o pagamento não existir
	 * @throws OrderNotFoundException
	 *             se o pedido não existir
	 */
	public static void ensureOrderPaymentIsValid(Long id, OrderStatus status, PaymentGateway paymentGateway,
			OrderGateway orderGateway) {
		var order = orderGateway.findById(id);
		PaymentStatusDTO payment = null;

		try {
			payment = paymentGateway.getOrderStatus(id);
		} catch (Exception ex) {
			log.info("Não foi possível obter o pagamento do pedido: {}, causa: {}", id, ex.getMessage());
		}

		if (order.isEmpty()) {
			throw new OrderNotFoundException("Pedido", id);
		} else if (payment == null && order.get().getOrderStatus() != OrderStatus.RECEIVED) {
			log.info("Pagamento do pedido de ID: {}, de status: {} não encontrado", id, order.get().getOrderStatus());
			throw new OrderPaymentNotFoundException("O pagamento do pedido não existe");
		}

		var isInProgress = status.getCode() > OrderStatus.RECEIVED.getCode()
				&& status.getCode() < OrderStatus.CANCELLED.getCode();
		var paymentNotRealized = payment != null && payment.status() != StatusDTO.APPROVED;

		if (isInProgress && paymentNotRealized) {
			throw new OrderPaymentException(
					String.format("Somente pedidos pagos podem transacionar para o status: %s", status));
		}
	}
}
