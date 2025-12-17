package com.soat.fiap.food.core.order.core.interfaceadapters.dto.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

/**
 * DTO utilizado para representar dados do evento de domínio
 * PaymentApprovedEvent. Serve como objeto de transferência entre o domínio e o
 * mundo externo (DataSource).
 */
@Data
public class PaymentApprovedEventDto {
	public UUID paymentId;
	public Long orderId;
	public BigDecimal amount;
	public String paymentMethod;
	public LocalDateTime approvedAt;
}
