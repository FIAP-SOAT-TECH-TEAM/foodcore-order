package com.soat.fiap.food.core.order.core.interfaceadapters.bff.controller.web.api;

import java.util.List;

import com.soat.fiap.food.core.order.core.application.usecases.GetActiveOrdersSortedUseCase;
import com.soat.fiap.food.core.order.core.interfaceadapters.bff.presenter.web.api.OrderPresenter;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.OrderGateway;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.response.OrderResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller: Buscar pedidos ativos ordenados por prioridade e data de criação.
 */
@Slf4j
public class GetActiveOrdersSortedController {

	/**
	 * Busca pedidos que não estejam finalizados, ordenados por prioridade de status
	 * e data de criação. A ordem de prioridade de status é: PRONTO > EM_PREPARACAO
	 * > RECEBIDO. Pedidos com status FINALIZADO não são retornados.
	 *
	 * @param dataSource
	 *            Origem de dados para o gateway
	 * @return Lista de pedidos ativos ordenados
	 */
	public static List<OrderResponse> getActiveOrdersSorted(OrderDataSource dataSource) {
		log.debug("Iniciando busca de pedidos ativos ordenados");

		var gateway = new OrderGateway(dataSource);

		var orders = GetActiveOrdersSortedUseCase.getActiveOrdersSorted(gateway);

		return OrderPresenter.toListOrderResponse(orders);
	}
}
