package com.soat.fiap.food.core.order.infrastructure.out.persistence.postgres.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.infrastructure.out.persistence.postgres.entity.OrderEntity;

/**
 * Repositório Spring Data JPA para OrderEntity
 */
@Repository
public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, Long> {

	/**
	 * Busca pedidos por status
	 *
	 * @param status
	 *            Status dos pedidos
	 * @return Lista de pedidos com o status informado
	 */
	List<OrderEntity> findByOrderStatus(OrderStatus status);

	/**
	 * Busca pedidos de um usuário específico
	 *
	 * @param userId
	 *            ID do usuário
	 * @return Lista de pedidos do cliente
	 */
	List<OrderEntity> findByUserId(String userId);

	/**
	 * Busca pedidos que não estejam finalizados, ordenados por prioridade de status
	 * e data de criação. A ordem de prioridade de status é: PRONTO > EM_PREPARACAO
	 * > RECEBIDO. Pedidos com status FINALIZADO não são retornados.
	 *
	 * @return Lista de pedidos ativos ordenados por prioridade de status e data de
	 *         criação (mais antigos primeiro)
	 */
	@Query("""
			SELECT o FROM OrderEntity o
			WHERE CAST(o.orderStatus AS string) <> 'COMPLETED' AND CAST(o.orderStatus AS string) <> 'CANCELLED'
			ORDER BY
			    CASE CAST(o.orderStatus AS string)
			        WHEN 'READY' THEN 1
			        WHEN 'PREPARING' THEN 2
			        WHEN 'RECEIVED' THEN 3
			        ELSE 4
			    END,
			    o.auditInfo.createdAt ASC
			""")
	List<OrderEntity> findActiveOrdersSorted();
}
