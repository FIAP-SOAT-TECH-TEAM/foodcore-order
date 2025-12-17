package com.soat.fiap.food.core.order.core.application.usecases;

import java.util.List;
import java.util.Objects;

import com.soat.fiap.food.core.order.core.domain.exceptions.OrderException;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderItemException;
import com.soat.fiap.food.core.order.core.domain.model.OrderItem;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.CatalogGateway;

import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso: Validar os itens do pedido.
 */
@Slf4j
public class EnsureValidOrderItemsUseCase {

	/**
	 * Valida os produtos dos itens do pedido com base no catálogo.
	 *
	 * @param orderItems
	 *            Lista de itens do pedido
	 * @param gateway
	 *            Gateway para comunicação com o mundo exterior (microsserviço de
	 *            Catalog (Product))
	 * @throws OrderException
	 *             se o produto não for encontrado
	 * @throws OrderItemException
	 *             se houver divergência nos dados dos produtos
	 */
	public static void ensureValidOrderItems(List<OrderItem> orderItems, CatalogGateway gateway) {

		var productIds = orderItems.stream().map(OrderItem::getProductId).toList();

		var products = gateway.findByProductIds(productIds);

		for (OrderItem orderItem : orderItems) {

			var productOrderItem = products.stream()
					.filter(p -> p.id() != null && Objects.equals(p.id(), orderItem.getProductId()))
					.findFirst()
					.orElseThrow(() -> new OrderItemException("O produto do item do pedido não existe"));

			if (!productOrderItem.name().equals(orderItem.getName())) {
				throw new OrderItemException("O nome do produto do item diverge do nome do produto cadastrado");
			} else if (productOrderItem.price().compareTo(orderItem.getUnitPrice()) != 0) {
				throw new OrderItemException("O preço unitário do item do pedido diverge do preço do produto");
			} else if (!productOrderItem.active()) {
				throw new OrderItemException("O pedido não pode possuir produtos inativos");
			} else if (!productOrderItem.categoryIsActive()) {
				throw new OrderItemException("A categoria do produto do pedido não pode estar inativa");
			} else if (productOrderItem.stock().quantity() < orderItem.getQuantity()) {
				throw new OrderItemException(String.format("Quantidade insuficiente em estoque para o produto: %s",
						productOrderItem.name()));
			}
		}
	}
}
