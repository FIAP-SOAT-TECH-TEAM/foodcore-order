package com.soat.fiap.food.core.order.core.interfaceadapters.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO utilizado para representar dados da entidade OrderItem. Serve como objeto
 * de transferência entre o domínio e o mundo externo (DataSource).
 */
public record OrderItemDTO(Long id, Long productId, String name, int quantity, BigDecimal price, String observations,
		LocalDateTime createdAt, LocalDateTime updatedAt) {

}
