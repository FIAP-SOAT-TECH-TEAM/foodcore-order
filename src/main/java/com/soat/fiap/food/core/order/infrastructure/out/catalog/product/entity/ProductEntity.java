package com.soat.fiap.food.core.order.infrastructure.out.catalog.product.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * Representa a entidade de Product na API do microsserviço de catálogo.
 */
@Data
public class ProductEntity {

	private Long id;

	private String name;

	private String description;

	private BigDecimal price;

	private String imageUrl;

	private boolean active;

	private boolean categoryIsActive;

	private Integer displayOrder;

	private StockEntity stock;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
