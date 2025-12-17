package com.soat.fiap.food.core.order.infrastructure.out.persistence.postgres.entity;

import com.soat.fiap.food.core.order.core.domain.vo.OrderItemPrice;
import com.soat.fiap.food.core.shared.core.domain.vo.AuditInfo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "order_items") @Getter @Setter
public class OrderItemEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "order_id", nullable = false)
	private OrderEntity order;

	@Column(name = "product_id", nullable = false)
	private Integer productId;

	@Column(nullable = false)
	private String name;

	@Embedded
	private OrderItemPrice orderItemPrice;

	@Column(columnDefinition = "TEXT")
	private String observations = "";

	@Embedded
	private AuditInfo auditInfo = new AuditInfo();
}
