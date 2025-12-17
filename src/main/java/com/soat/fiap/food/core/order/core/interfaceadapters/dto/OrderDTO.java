package com.soat.fiap.food.core.order.core.interfaceadapters.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;

/**
 * DTO utilizado para representar dados da entidade Order. Serve como objeto de
 * transferência entre o domínio e o mundo externo (DataSource).
 */
public record OrderDTO(Long id, String userId, String orderNumber, OrderStatus status, BigDecimal amount,
		List<OrderItemDTO> items, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
