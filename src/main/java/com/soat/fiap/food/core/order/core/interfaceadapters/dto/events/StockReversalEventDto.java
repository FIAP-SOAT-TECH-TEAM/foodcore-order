package com.soat.fiap.food.core.order.core.interfaceadapters.dto.events;

import lombok.Data;
/**
 * DTO utilizado para representar dados do evento de domínio StockReversalEvent.
 * Serve como objeto de transferência entre o domínio e o mundo externo
 * (DataSource).
 */
@Data
public class StockReversalEventDto {
	private Long orderId;
}
