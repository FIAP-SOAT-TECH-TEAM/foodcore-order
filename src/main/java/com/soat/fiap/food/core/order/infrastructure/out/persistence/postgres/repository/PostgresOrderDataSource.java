package com.soat.fiap.food.core.order.infrastructure.out.persistence.postgres.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.OrderDTO;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.out.persistence.postgres.entity.OrderEntity;
import com.soat.fiap.food.core.order.infrastructure.out.persistence.postgres.entity.OrderItemEntity;
import com.soat.fiap.food.core.order.infrastructure.out.persistence.postgres.mapper.OrderEntityMapper;

/**
 * Implementação concreta: DataSource para persistência do agregado Pedido.
 */
@Component
public class PostgresOrderDataSource implements OrderDataSource {

	private final SpringDataOrderRepository springDataOrderRepository;
	private final OrderEntityMapper orderEntityMapper;

	public PostgresOrderDataSource(SpringDataOrderRepository springDataOrderRepository,
			OrderEntityMapper orderEntityMapper) {
		this.springDataOrderRepository = springDataOrderRepository;
		this.orderEntityMapper = orderEntityMapper;
	}

	@Override @Transactional
	public OrderDTO save(OrderDTO orderDTO) {
		OrderEntity orderEntity = orderEntityMapper.toEntity(orderDTO);
		for (OrderItemEntity item : orderEntity.getOrderItems()) {
			item.setOrder(orderEntity);
		}
		OrderEntity savedEntity = springDataOrderRepository.save(orderEntity);
		return orderEntityMapper.toDTO(savedEntity);
	}

	@Override @Transactional(readOnly = true)
	public Optional<OrderDTO> findById(Long id) {
		return springDataOrderRepository.findById(id).map(orderEntityMapper::toDTO);
	}

	@Override @Transactional(readOnly = true)
	public List<OrderDTO> findByOrderStatus(OrderStatus status) {
		List<OrderEntity> orderEntities = springDataOrderRepository.findByOrderStatus(status);
		return orderEntities.stream().map(orderEntityMapper::toDTO).toList();
	}

	@Override @Transactional(readOnly = true)
	public List<OrderDTO> findByUserId(String userId) {
		List<OrderEntity> orderEntities = springDataOrderRepository.findByUserId(userId);
		return orderEntities.stream().map(orderEntityMapper::toDTO).toList();
	}

	@Override @Transactional(readOnly = true)
	public List<OrderDTO> findAll() {
		List<OrderEntity> orderEntities = springDataOrderRepository.findAll();
		return orderEntities.stream().map(orderEntityMapper::toDTO).toList();
	}

	@Override @Transactional
	public void delete(Long id) {
		springDataOrderRepository.deleteById(id);
	}

	@Override @Transactional(readOnly = true)
	public List<OrderDTO> findActiveOrdersSorted() {
		List<OrderEntity> orderEntities = springDataOrderRepository.findActiveOrdersSorted();
		return orderEntities.stream().map(orderEntityMapper::toDTO).toList();
	}
}
