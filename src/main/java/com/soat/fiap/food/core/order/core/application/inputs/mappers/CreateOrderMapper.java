package com.soat.fiap.food.core.order.core.application.inputs.mappers;

import com.soat.fiap.food.core.order.core.application.inputs.CreateOrderInput;
import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.CreateOrderRequest;
import com.soat.fiap.food.core.shared.core.interfaceadapters.gateways.AuthenticatedUserGateway;

/**
 * Classe utilitária responsável por mapear objetos entre
 * {@link CreateOrderRequest} e {@link CreateOrderInput}.
 */
public class CreateOrderMapper {

	/**
	 * Converte um {@link CreateOrderRequest} (camada web.api) em um
	 * {@link CreateOrderInput} (camada de aplicação).
	 *
	 * @param request
	 *            O DTO da requisição HTTP para criação de pedido.
	 * @param authenticatedUserGateway
	 *            Gateway de usuário autenticado
	 * @return Um objeto {@link CreateOrderInput} com os dados para processar a
	 *         criação do pedido.
	 */
	public static CreateOrderInput toInput(CreateOrderRequest request,
			AuthenticatedUserGateway authenticatedUserGateway) {

		var userId = authenticatedUserGateway.getSubject();

		return new CreateOrderInput(userId, request.getItems().stream().map(OrderItemMapper::toInput).toList());
	}

	/**
	 * Converte um {@link CreateOrderInput} (camada de aplicação) em uma entidade de
	 * domínio {@link Order}.
	 *
	 * @param input
	 *            O DTO de entrada da aplicação.
	 * @return Uma entidade {@link Order} representando o modelo de domínio para
	 *         persistência e regras de negócio.
	 */
	public static Order toDomain(CreateOrderInput input) {
		return new Order(input.userId(), input.items().stream().map(OrderItemMapper::toDomain).toList());
	}
}
