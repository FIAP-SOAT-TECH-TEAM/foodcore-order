package com.soat.fiap.food.core.order.core.interfaceadapters.dto.mappers;

import com.soat.fiap.food.core.order.core.domain.events.OrderReadyEvent;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderReadyEventDto;

/**
 * Classe utilitária responsável por mapear {@link OrderReadyEvent} para o DTO
 * {@link OrderReadyEventDto}, utilizado para transporte de dados do evento de
 * pedido pronto.
 */
public class OrderReadyEventMapper {

	/**
	 * Converte um {@link OrderReadyEvent} em um {@link OrderReadyEventDto}.
	 *
	 * @param event
	 *            Evento de pedido pronto.
	 * @return DTO com os dados do pedido pronto.
	 */
	public static OrderReadyEventDto toDto(OrderReadyEvent event) {
		if (event == null) {
			return null;
		}

		OrderReadyEventDto dto = new OrderReadyEventDto();

		dto.setClientId(event.getClientId());
		dto.setOrderNumber(event.getOrderNumber());
		dto.setAmount(event.getAmount());
		dto.setReadyAt(event.getReadyAt());

		return dto;
	}
}
