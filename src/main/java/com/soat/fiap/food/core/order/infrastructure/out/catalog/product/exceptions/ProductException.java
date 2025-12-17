package com.soat.fiap.food.core.order.infrastructure.out.catalog.product.exceptions;

import com.soat.fiap.food.core.shared.infrastructure.out.exceptions.APIException;

/**
 * Exceção lançada quando existe um erro de retorno da API do microsserviço de
 * Catalog (Product)
 */
public class ProductException extends APIException {

	public ProductException(String message, int statusCode) {
		super(message, statusCode);
	}

	public ProductException(String message, Throwable cause, int statusCode) {
		super(message, cause, statusCode);
	}
}
