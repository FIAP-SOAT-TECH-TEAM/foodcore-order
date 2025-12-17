package com.soat.fiap.food.core.order.core.interfaceadapters.dto.events;

import java.math.BigDecimal;
import java.util.List;

import com.soat.fiap.food.core.order.core.domain.events.OrderCreatedEvent;

import lombok.Data;

/**
 * DTO utilizado para representar dados do evento de domínio
 * {@link OrderCreatedEvent}. Serve como objeto de transferência entre o domínio
 * e o mundo externo (DataSource).
 */
@Data
public class OrderCreatedEventDto {
	public Long id;
	public String orderNumber;
	public String statusDescription;
	public String userId;
	public BigDecimal totalAmount;
	public List<OrderItemCreatedEventDto> items;
}
