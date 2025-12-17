package com.soat.fiap.food.core.order.core.interfaceadapters.bff.controller.web.api;

import com.soat.fiap.food.core.order.core.application.usecases.GetOrderByIdUseCase;
import com.soat.fiap.food.core.order.core.interfaceadapters.bff.presenter.web.api.OrderPresenter;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.OrderGateway;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.response.OrderResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller: Buscar pedido por ID.
 *
 * Este controller é responsável por buscar um pedido específico pelo seu ID.
 * Ele utiliza o caso de uso GetOrderByIdUseCase para realizar a busca e
 * converte o resultado para a resposta adequada usando OrderPresenter.
 */
@Slf4j
public class GetOrderByIdController {
	/**
	 * Busca um pedido pelo seu ID.
	 *
	 * @param id
	 *            Identificador do pedido
	 * @param orderDataSource
	 *            Origem de dados para o gateway
	 * @return o catálogo
	 */
	public static OrderResponse getOrderById(Long id, OrderDataSource orderDataSource) {
		log.debug("Buscando pedido de id: {}", id);

		var gateway = new OrderGateway(orderDataSource);

		var existingOrder = GetOrderByIdUseCase.getOrderById(id, gateway);

		return OrderPresenter.toOrderResponse(existingOrder);
	}
}
