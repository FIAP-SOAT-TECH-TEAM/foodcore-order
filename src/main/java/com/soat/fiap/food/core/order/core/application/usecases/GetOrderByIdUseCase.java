package com.soat.fiap.food.core.order.core.application.usecases;

import com.soat.fiap.food.core.order.core.domain.exceptions.OrderNotFoundException;
import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.OrderGateway;

import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso: Obter pedido por identificador.
 */
@Slf4j
public class GetOrderByIdUseCase {

	/**
	 * Busca um pedido pelo seu ID.
	 *
	 * @param id
	 *            Identificador do pedido
	 * @param gateway
	 *            Gateway para comunicação com o mundo exterior
	 * @return o pedido
	 */
	public static Order getOrderById(Long id, OrderGateway gateway) {
		var existingOrder = gateway.findById(id);

		if (existingOrder.isEmpty()) {
			log.warn("Pedido não encontrado. Id: {}", id);
			throw new OrderNotFoundException("Pedido", id);
		}

		return existingOrder.get();
	}
}
