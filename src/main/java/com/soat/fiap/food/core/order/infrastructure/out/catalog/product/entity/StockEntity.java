package com.soat.fiap.food.core.order.infrastructure.out.catalog.product.entity;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Representa a entidade de Stock na API do microsserviço de catálogo.
 */
@Data
public class StockEntity {
	private Long id;

	private Integer quantity;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
