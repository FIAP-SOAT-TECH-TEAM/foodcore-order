package com.soat.fiap.food.core.order.core.domain.exceptions;

import com.soat.fiap.food.core.shared.core.domain.exceptions.BusinessException;

/**
 * Exceção lançada quando uma regra de negócio é violada na entidade item do
 * pedido
 */
public class OrderPaymentException extends BusinessException {

	public OrderPaymentException(String message) {
		super(message);
	}

	public OrderPaymentException(String message, Throwable cause) {
		super(message, cause);
	}
}
