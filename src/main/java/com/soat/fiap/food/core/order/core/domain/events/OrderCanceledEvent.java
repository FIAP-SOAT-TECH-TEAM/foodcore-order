package com.soat.fiap.food.core.order.core.domain.events;

import java.util.List;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;

import lombok.Data;

/**
 * Evento de domínio (DDD) emitido quando um pedido é cancelado
 */
@Data
public class OrderCanceledEvent {

	private Long id;

	private OrderStatus status;

	private List<OrderItemCanceledEvent> items;

}
