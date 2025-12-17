package com.soat.fiap.food.core.order.core.interfaceadapters.dto.mappers;

import java.util.List;

import com.soat.fiap.food.core.order.core.domain.events.OrderCreatedEvent;
import com.soat.fiap.food.core.order.core.domain.events.OrderItemCreatedEvent;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderItemCreatedEventDto;

/**
 * Classe utilitária responsável por mapear {@link OrderCreatedEvent} para o DTO
 * {@link OrderCreatedEventDto}, utilizado para transporte de dados do evento de
 * pedido criado.
 */
public class OrderCreatedEventMapper {

	/**
	 * Converte um {@link OrderCreatedEvent} em um {@link OrderCreatedEventDto}.
	 *
	 * @param event
	 *            Evento de criação de pedido.
	 * @return DTO com os dados do pedido criado.
	 */
	public static OrderCreatedEventDto toDto(OrderCreatedEvent event) {
		List<OrderItemCreatedEventDto> itemDtos = event.getItems()
				.stream()
				.map(OrderCreatedEventMapper::toItemDto)
				.toList();

		OrderCreatedEventDto dto = new OrderCreatedEventDto();
		dto.setId(event.getId());
		dto.setOrderNumber(event.getOrderNumber());
		dto.setStatusDescription(event.getStatusDescription());
		dto.setUserId(event.getUserId());
		dto.setTotalAmount(event.getTotalAmount());
		dto.setItems(itemDtos);

		return dto;
	}

	/**
	 * Converte um item do evento {@link OrderItemCreatedEvent} em seu respectivo
	 * DTO {@link OrderItemCreatedEventDto}.
	 *
	 * @param item
	 *            Evento de item do pedido criado.
	 * @return DTO representando o item criado.
	 */
	private static OrderItemCreatedEventDto toItemDto(OrderItemCreatedEvent item) {
		OrderItemCreatedEventDto dto = new OrderItemCreatedEventDto();
		dto.setId(item.getId());
		dto.setProductId(item.getProductId());
		dto.setName(item.getName());
		dto.setQuantity(item.getQuantity());
		dto.setUnitPrice(item.getUnitPrice());
		dto.setSubtotal(item.getSubtotal());
		dto.setObservations(item.getObservations());
		return dto;
	}
}
