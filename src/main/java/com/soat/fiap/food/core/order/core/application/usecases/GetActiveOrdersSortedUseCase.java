package com.soat.fiap.food.core.order.core.application.usecases;

import java.util.List;

import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.OrderGateway;

import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso: Buscar pedidos ativos ordenados por prioridade e data de
 * criação.
 */
@Slf4j
public class GetActiveOrdersSortedUseCase {

	/**
	 * Busca pedidos que não estejam finalizados, ordenados por prioridade de status
	 * e data de criação. A ordem de prioridade de status é: PRONTO > EM_PREPARACAO
	 * > RECEBIDO. Pedidos com status FINALIZADO não são retornados.
	 *
	 * @param gateway
	 *            Gateway para comunicação com o mundo exterior
	 * @return Pedidos ativos ordenados por odem de prioridade
	 */
	public static List<Order> getActiveOrdersSorted(OrderGateway gateway) {
		log.info("Buscando pedidos ativos ordenados por prioridade e data de criação.");

		return gateway.findActiveOrdersSorted();
	}
}
