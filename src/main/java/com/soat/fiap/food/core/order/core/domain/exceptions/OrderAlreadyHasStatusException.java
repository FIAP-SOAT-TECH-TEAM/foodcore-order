package com.soat.fiap.food.core.order.core.domain.exceptions;

import com.soat.fiap.food.core.shared.core.domain.exceptions.BusinessException;

/**
 * Exceção lançada quando um pedido já possui o status informado para
 * atualização
 */
public class OrderAlreadyHasStatusException extends BusinessException {

	public OrderAlreadyHasStatusException(String message) {
		super(message);
	}

	public OrderAlreadyHasStatusException(String message, Throwable cause) {
		super(message, cause);
	}
}
