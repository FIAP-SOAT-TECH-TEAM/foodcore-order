package com.soat.fiap.food.core.order.core.domain.events;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Evento de domínio (DDD) emitido quando um pedido é criado
 */
@Data
public class OrderItemCreatedEvent {

	private Long id;

	private Long productId;

	private String name;

	private Integer quantity;

	private BigDecimal unitPrice;

	private BigDecimal subtotal;

	private String observations;
}
