package com.soat.fiap.food.core.order.core.interfaceadapters.dto.events;

import java.math.BigDecimal;

import com.soat.fiap.food.core.order.core.domain.events.OrderReadyEvent;

import lombok.Data;

/**
 * DTO utilizado para representar dados do evento de domínio
 * {@link OrderReadyEvent}. Serve como objeto de transferência entre o domínio e
 * o mundo externo (DataSource).
 */
@Data
public class OrderReadyEventDto {
	private String clientId;
	private String orderNumber;
	private BigDecimal amount;
	private String readyAt;
}
