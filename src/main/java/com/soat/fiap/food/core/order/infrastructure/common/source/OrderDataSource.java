package com.soat.fiap.food.core.order.infrastructure.common.source;

import java.util.List;
import java.util.Optional;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.OrderDTO;

/**
 * DataSource para persistência de pedidos
 */
public interface OrderDataSource {

	/**
	 * Salva um pedido
	 *
	 * @param orderDTO
	 *            Pedido a ser salvo
	 * @return Pedido salvo com ID gerado
	 */
	OrderDTO save(OrderDTO orderDTO);

	/**
	 * Busca um pedido por ID
	 *
	 * @param id
	 *            ID do pedido
	 * @return Optional contendo o pedido ou vazio se não encontrado
	 */
	Optional<OrderDTO> findById(Long id);

	/**
	 * Busca pedidos por status
	 *
	 * @param status
	 *            Status dos pedidos
	 * @return Lista de pedidos com o status informado
	 */
	List<OrderDTO> findByOrderStatus(OrderStatus status);

	/**
	 * Busca pedidos de um cliente específico
	 *
	 * @param userId
	 *            ID do cliente
	 * @return Lista de pedidos do cliente
	 */
	List<OrderDTO> findByUserId(String userId);

	/**
	 * Lista todos os pedidos
	 *
	 * @return Lista de pedidos
	 */
	List<OrderDTO> findAll();

	/**
	 * Remove um pedido
	 *
	 * @param id
	 *            ID do pedido a ser removido
	 */
	void delete(Long id);

	/**
	 * Busca pedidos que não estejam finalizados, ordenados por prioridade de status
	 * e data de criação. A ordem de prioridade de status é: PRONTO > EM_PREPARACAO
	 * > RECEBIDO. Pedidos com status FINALIZADO não são retornados.
	 *
	 * @return Lista de pedidos ativos ordenados por prioridade de status e data de
	 *         criação (mais antigos primeiro)
	 */
	List<OrderDTO> findActiveOrdersSorted();

}
