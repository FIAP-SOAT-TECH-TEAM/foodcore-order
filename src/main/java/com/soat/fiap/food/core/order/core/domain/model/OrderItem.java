package com.soat.fiap.food.core.order.core.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import com.soat.fiap.food.core.order.core.domain.exceptions.OrderItemException;
import com.soat.fiap.food.core.order.core.domain.vo.OrderItemPrice;
import com.soat.fiap.food.core.shared.core.domain.vo.AuditInfo;

import lombok.Getter;
import lombok.Setter;

/**
 * Entidade de domínio que representa um item de pedido
 */
@Getter @Setter
public class OrderItem {

	private Long id;
	private Long productId;
	private String name;
	private OrderItemPrice orderItemPrice;
	private String observations = "";

	private AuditInfo auditInfo = new AuditInfo();
	private Order order;

	/**
	 * Construtor que cria um item de pedido com os dados informados
	 *
	 * @param productId
	 *            ID do produto
	 * @param name
	 *            Nome do item do pedido
	 * @param orderItemPrice
	 *            Preço do item
	 * @param observations
	 *            Observações sobre o item
	 * @throws NullPointerException
	 *             se qualquer parâmetro for nulo
	 */
	public OrderItem(Long productId, String name, OrderItemPrice orderItemPrice, String observations) {
		validate(productId, name, orderItemPrice);

		this.productId = productId;
		this.name = name;
		this.orderItemPrice = orderItemPrice;
		this.observations = observations;
	}

	/**
	 * Validação centralizada.
	 *
	 * @param productId
	 *            ID do produto
	 * @param orderItemPrice
	 *            Preço do item
	 * @param name
	 *            Nome do item do pedido
	 * @throws NullPointerException
	 *             se qualquer parâmetro for nulo
	 */
	private void validate(Long productId, String name, OrderItemPrice orderItemPrice) {
		Objects.requireNonNull(productId, "O ID do produto não pode ser nulo");
		Objects.requireNonNull(orderItemPrice, "O preço do item do pedido não pode ser nulo");

		if (name.isEmpty()) {
			throw new OrderItemException("O nome do item do pedido não pode ser nulo");
		}
	}

	/**
	 * Retorna o id da orderm do item de pedido
	 */
	public Long getOrderId() {
		return this.order.getId();
	}

	/**
	 * Retorna o subtotal do item de pedido com base no preço e quantidade
	 *
	 * @return subtotal do item
	 */
	public BigDecimal getSubTotal() {
		return this.orderItemPrice.getSubTotal();
	}

	/**
	 * Retorna o preço do item do pedido
	 */
	public BigDecimal getUnitPrice() {
		return orderItemPrice.unitPrice();
	}

	/**
	 * Retorna a quantidade do item do pedido
	 */
	public Integer getQuantity() {
		return orderItemPrice.quantity();
	}

	/**
	 * Obtem a data de criação de um item de pedido.
	 */
	public LocalDateTime getCreatedAt() {
		return auditInfo.getCreatedAt();
	}

	/**
	 * Obtem a data de última atualização de um item de pedido.
	 */
	public LocalDateTime getUpdatedAt() {
		return auditInfo.getUpdatedAt();
	}
}
