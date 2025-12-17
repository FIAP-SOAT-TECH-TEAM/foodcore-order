package com.soat.fiap.food.core.order.infrastructure.out.persistence.postgres.mapper.shared;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import com.soat.fiap.food.core.order.core.domain.vo.OrderItemPrice;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.OrderItemDTO;

@Mapper(componentModel = "default")
public interface OrderItemPriceMapper {

	@Named("fromQuantityAndPrice")
	static OrderItemPrice fromQuantityAndPrice(OrderItemDTO dto) {
		return new OrderItemPrice(dto.quantity(), dto.price());
	}

	@Named("extractQuantity")
	static int extractQuantity(OrderItemPrice price) {
		return price != null ? price.quantity() : 0;
	}

	@Named("extractPrice")
	static BigDecimal extractPrice(OrderItemPrice price) {
		return price != null ? price.unitPrice() : null;
	}
}
