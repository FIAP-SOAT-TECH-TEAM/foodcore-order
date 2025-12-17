package com.soat.fiap.food.core.order.infrastructure.out.payment.entity;

import com.soat.fiap.food.core.order.core.interfaceadapters.dto.payment.StatusDTO;

import lombok.Data;

/**
 * Representa o contrato de obtenção do status de pagamento de um pedido, na API
 * do microsserviço de pagamento.
 */
@Data
public class PaymentStatusEntity {
	private Long orderId;
	private StatusDTO status;
}
