package com.soat.fiap.food.core.order.core.interfaceadapters.gateways;

import java.util.List;
import java.util.Optional;

import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.OrderDTO;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.mappers.OrderDTOMapper;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;

/**
 * Gateway para persistência do agregado Pedido.
 */
public class OrderGateway {

	private final OrderDataSource orderDataSource;

	public OrderGateway(OrderDataSource orderDataSource) {
		this.orderDataSource = orderDataSource;
	}

	/**
	 * Salva o agregado Pedido.
	 *
	 * @param order
	 *            Agregado Pedido a ser salvo
	 * @return Pedido salvo com identificadores atualizados
	 */
	public Order save(Order order) {
		OrderDTO dto = OrderDTOMapper.toDTO(order);
		OrderDTO savedDTO = orderDataSource.save(dto);
		return OrderDTOMapper.toDomain(savedDTO);
	}

	/**
	 * Busca um pedido pelo ID.
	 *
	 * @param id
	 *            ID do pedido
	 * @return Optional contendo o pedido ou vazio se não encontrado
	 */
	public Optional<Order> findById(Long id) {
		return orderDataSource.findById(id).map(OrderDTOMapper::toDomain);
	}
	/**
	 * Lista todos os pedidos persistidos.
	 *
	 * @return Lista de pedidos
	 */
	public List<Order> findAll() {
		return orderDataSource.findAll().stream().map(OrderDTOMapper::toDomain).toList();
	}

	/**
	 * Remove um pedido com base em seu ID.
	 *
	 * @param id
	 *            ID do pedido a ser removido
	 */
	public void delete(Long id) {
		orderDataSource.delete(id);
	}

	/**
	 * Busca todos os pedidos com um determinado status.
	 *
	 * @param status
	 *            Status do pedido
	 * @return Lista de pedidos com o status informado
	 */
	public List<Order> findByOrderStatus(OrderStatus status) {
		return orderDataSource.findByOrderStatus(status).stream().map(OrderDTOMapper::toDomain).toList();
	}

	/**
	 * Busca todos os pedidos de um determinado cliente.
	 *
	 * @param userId
	 *            ID do cliente
	 * @return Lista de pedidos do cliente
	 */
	public List<Order> findByUserId(String userId) {
		return orderDataSource.findByUserId(userId).stream().map(OrderDTOMapper::toDomain).toList();
	}

	/**
	 * Busca pedidos ativos, ordenados por prioridade de status e data de criação.
	 * <p>
	 * Prioridade de status: PRONTO > EM_PREPARACAO > RECEBIDO. Pedidos com status
	 * FINALIZADO não são incluídos.
	 *
	 * @return Lista de pedidos ativos ordenados
	 */
	public List<Order> findActiveOrdersSorted() {
		return orderDataSource.findActiveOrdersSorted().stream().map(OrderDTOMapper::toDomain).toList();
	}
}
