package com.soat.fiap.food.core.order.core.interfaceadapters.dto.mappers;

import java.util.List;

import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.core.domain.model.OrderItem;
import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.OrderDTO;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.OrderItemDTO;
import com.soat.fiap.food.core.shared.core.domain.vo.AuditInfo;

/**
 * Mapper responsável por mapear entre a entidade de domínio Order e seu
 * correspondente DTO.
 */
public class OrderDTOMapper {

	/**
	 * Cria uma instância de {@link Order} a partir de um {@link OrderDTO}.
	 *
	 * @param dto
	 *            DTO de pedido contendo os dados a serem convertidos
	 * @return Instância de um Order{@link Order}
	 */
	public static Order toDomain(OrderDTO dto) {
		List<OrderItem> items = dto.items().stream().map(OrderItemDTOMapper::toDomain).toList();

		Order order = new Order(dto.userId(), items);

		order.setId(dto.id());

		if (dto.status() != null && dto.status() != OrderStatus.RECEIVED) {
			order.setOrderStatus(dto.status());
		}

		if (dto.createdAt() != null && dto.updatedAt() != null) {
			order.setAuditInfo(new AuditInfo(dto.createdAt(), dto.updatedAt()));
		}

		return order;
	}

	/**
	 * Cria uma instância de {@link OrderDTO} a partir de um {@link Order}.
	 *
	 * @param order
	 *            Entidade de domínio de pedido contendo os dados a serem
	 *            convertidos
	 * @return Instância do DTO de pedido {@link OrderDTO}
	 */
	public static OrderDTO toDTO(Order order) {
		List<OrderItemDTO> itemDTOs = order.getOrderItems()
				.stream()
				.map(item -> new OrderItemDTO(item.getId(), item.getProductId(), item.getName(),
						item.getOrderItemPrice().quantity(), item.getOrderItemPrice().unitPrice(),
						item.getObservations(), item.getCreatedAt(), item.getUpdatedAt()))
				.toList();

		return new OrderDTO(order.getId(), order.getUserId(), order.getOrderNumber(), order.getOrderStatus(),
				order.getAmount(), itemDTOs, order.getCreatedAt(), order.getUpdatedAt());
	}
}
