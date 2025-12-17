package com.soat.fiap.food.core.order.infrastructure.out.persistence.postgres.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.soat.fiap.food.core.order.core.interfaceadapters.dto.OrderDTO;
import com.soat.fiap.food.core.order.infrastructure.out.persistence.postgres.entity.OrderEntity;
import com.soat.fiap.food.core.order.infrastructure.out.persistence.postgres.mapper.shared.OrderNumberMapper;
import com.soat.fiap.food.core.shared.infrastructure.common.mapper.CycleAvoidingMappingContext;
import com.soat.fiap.food.core.shared.infrastructure.common.mapper.DoIgnore;

/**
 * Mapper que converte entre a entidade de domínio Order e a entidade JPA
 * OrderEntity
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {OrderItemEntityMapper.class,
		OrderNumberMapper.class})
public interface OrderEntityMapper {

	@Mapping(target = "orderItems", source = "items") @Mapping(target = "orderStatus", source = "status")
	@Mapping(target = "auditInfo", expression = "java(com.soat.fiap.food.core.shared.infrastructure.common.mapper.AuditInfoMapper.buildAuditInfo(dto.createdAt(), dto.updatedAt()))")
	OrderEntity toEntity(OrderDTO dto, @Context CycleAvoidingMappingContext context);

	@Mapping(target = "items", source = "orderItems") @Mapping(target = "status", source = "orderStatus")
	@Mapping(target = "createdAt", source = "auditInfo.createdAt")
	@Mapping(target = "updatedAt", source = "auditInfo.updatedAt")
	OrderDTO toDTO(OrderEntity entity, @Context CycleAvoidingMappingContext context);

	@DoIgnore
	default OrderEntity toEntity(OrderDTO dto) {
		return toEntity(dto, new CycleAvoidingMappingContext());
	}

	@DoIgnore
	default OrderDTO toDTO(OrderEntity entity) {
		return toDTO(entity, new CycleAvoidingMappingContext());
	}

}
