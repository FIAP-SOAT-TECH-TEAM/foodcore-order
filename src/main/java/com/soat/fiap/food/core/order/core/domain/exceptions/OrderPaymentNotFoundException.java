package com.soat.fiap.food.core.order.core.domain.exceptions;

import com.soat.fiap.food.core.shared.core.domain.exceptions.BusinessException;

/**
 * Exceção lançada quando o pagamento de um pedido não é encontrado
 */
public class OrderPaymentNotFoundException extends BusinessException {

	public OrderPaymentNotFoundException(String message) {
		super(message);
	}

	public OrderPaymentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
