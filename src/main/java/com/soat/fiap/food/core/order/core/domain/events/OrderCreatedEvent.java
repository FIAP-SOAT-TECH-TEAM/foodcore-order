package com.soat.fiap.food.core.order.core.domain.events;

import java.math.BigDecimal;
import java.util.List;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;

import lombok.Data;

/**
 * Evento de domínio (DDD) emitido quando um pedido é criado
 */
@Data
public class OrderCreatedEvent {

	private Long id;

	private String orderNumber;

	private OrderStatus status;

	private String statusDescription;

	private String userId;

	private BigDecimal totalAmount;

	private List<OrderItemCreatedEvent> items;

}
