package com.soat.fiap.food.core.order.core.application.inputs.mappers;

import com.soat.fiap.food.core.order.core.application.inputs.OrderItemInput;
import com.soat.fiap.food.core.order.core.domain.model.OrderItem;
import com.soat.fiap.food.core.order.core.domain.vo.OrderItemPrice;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.OrderItemRequest;

/**
 * Classe utilitária responsável por mapear objetos entre
 * {@link OrderItemRequest} e {@link OrderItemInput}.
 */
public class OrderItemMapper {

	/**
	 * Converte um {@link OrderItemRequest} (camada web.api) em um
	 * {@link OrderItemInput} (camada de aplicação).
	 *
	 * @param request
	 *            O DTO de item do pedido recebido via API.
	 * @return Um objeto {@link OrderItemInput} contendo os dados necessários.
	 */
	public static OrderItemInput toInput(OrderItemRequest request) {
		return new OrderItemInput(request.getProductId(), request.getName(), request.getQuantity(),
				request.getUnitPrice(), request.getObservations());
	}

	/**
	 * Converte um {@link OrderItemInput} (camada de aplicação) em uma entidade de
	 * domínio {@link OrderItem}.
	 *
	 * @param input
	 *            O DTO de entrada representando o item do pedido.
	 * @return Uma entidade {@link OrderItem} para manipulação dentro do domínio.
	 */
	public static OrderItem toDomain(OrderItemInput input) {
		return new OrderItem(input.productId(), input.name(), new OrderItemPrice(input.quantity(), input.unitPrice()),
				input.observations());
	}
}
