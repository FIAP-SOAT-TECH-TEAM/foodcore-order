package com.soat.fiap.food.core.order.core.domain.exceptions;

import com.soat.fiap.food.core.shared.core.domain.exceptions.ResourceNotFoundException;

/**
 * Exceção lançada quando um pedido não é encontrado
 */
public class OrderNotFoundException extends ResourceNotFoundException {

	public OrderNotFoundException(String message) {
		super(message);
	}

	public OrderNotFoundException(String message, Long id) {
		super(message, id);
	}
}
