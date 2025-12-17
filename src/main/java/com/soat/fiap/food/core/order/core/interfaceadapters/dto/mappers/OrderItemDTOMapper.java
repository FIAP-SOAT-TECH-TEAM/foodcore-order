package com.soat.fiap.food.core.order.core.interfaceadapters.dto.mappers;

import java.util.Objects;

import com.soat.fiap.food.core.order.core.domain.model.OrderItem;
import com.soat.fiap.food.core.order.core.domain.vo.OrderItemPrice;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.OrderItemDTO;
import com.soat.fiap.food.core.shared.core.domain.vo.AuditInfo;

/**
 * Mapper responsável por mapear entre a entidade de domínio OrderItem e seu
 * correspondente DTO.
 */
public class OrderItemDTOMapper {

	/**
	 * Construtor que cria um item de pedido com os dados informados via DTO
	 *
	 * @param dto
	 *            DTO que contém os dados do item do pedido
	 * @throws NullPointerException
	 *             se qualquer parâmetro for nulo
	 */
	public static OrderItem toDomain(OrderItemDTO dto) {
		Objects.requireNonNull(dto, "O DTO do item do pedido não pode ser nulo");

		OrderItemPrice price = new OrderItemPrice(dto.quantity(), dto.price());

		OrderItem item = new OrderItem(dto.productId(), dto.name(), price, dto.observations());
		item.setId(dto.id());

		if (dto.createdAt() != null && dto.updatedAt() != null) {
			item.setAuditInfo(new AuditInfo(dto.createdAt(), dto.updatedAt()));
		}

		return item;
	}
}
