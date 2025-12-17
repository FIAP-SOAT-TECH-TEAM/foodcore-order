package com.soat.fiap.food.core.order.core.domain.exceptions;

import com.soat.fiap.food.core.shared.core.domain.exceptions.ResourceNotFoundException;

/**
 * Exceção lançada quando um item de pedido não é encontrado
 */
public class OrderItemNotFoundException extends ResourceNotFoundException {

	public OrderItemNotFoundException(String message) {
		super(message);
	}

	public OrderItemNotFoundException(String message, Long id) {
		super(message, id);
	}
}
