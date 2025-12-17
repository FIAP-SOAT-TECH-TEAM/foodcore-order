package com.soat.fiap.food.core.order.core.application.inputs;

import java.math.BigDecimal;

/**
 * Representa um DTO de entrada da aplicação (Application Layer) contendo os
 * dados de um item de pedido.
 */
public record OrderItemInput(Long productId, String name, Integer quantity, BigDecimal unitPrice, String observations) {
	/**
	 * Construtor do record {@code OrderItemInput}.
	 *
	 * @param productId
	 *            ID do produto.
	 * @param name
	 *            Nome do produto.
	 * @param quantity
	 *            Quantidade do produto.
	 * @param unitPrice
	 *            Preço unitário.
	 * @param observations
	 *            Observações adicionais (opcional).
	 */
	public OrderItemInput {
	}
}
