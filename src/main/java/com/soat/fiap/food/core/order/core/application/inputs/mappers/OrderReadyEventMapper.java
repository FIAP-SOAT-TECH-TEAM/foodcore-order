package com.soat.fiap.food.core.order.core.application.inputs.mappers;

import com.soat.fiap.food.core.order.core.domain.events.OrderReadyEvent;
import com.soat.fiap.food.core.order.core.domain.model.Order;

/**
 * Classe utilitária responsável por mapear uma entidade de domínio
 * {@link Order} para o evento {@link OrderReadyEvent}.
 */
public class OrderReadyEventMapper {

	/**
	 * Converte uma entidade de domínio {@link Order} em um evento
	 * {@link OrderReadyEvent}, mapeando seus atributos principais.
	 *
	 * @param order
	 *            A entidade {@link Order} a ser convertida.
	 * @return Um evento {@link OrderReadyEvent} com os dados do pedido pronto.
	 */
	public static OrderReadyEvent toEvent(Order order) {
		if (order == null) {
			return null;
		}

		OrderReadyEvent event = new OrderReadyEvent();
		event.setClientId(order.getUserId());
		event.setOrderNumber(order.getOrderNumber());
		event.setAmount(order.getAmount());

		return event;
	}
}
