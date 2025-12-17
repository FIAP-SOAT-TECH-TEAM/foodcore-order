package com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para atualização de status de um pedido
 */
@Data @NoArgsConstructor @AllArgsConstructor @Schema(description = "Requisição para atualização de status de um pedido")
public class OrderStatusRequest {

	@NotNull(message = "O status é obrigatório") @Schema(description = "Novo status do pedido", example = "PREPARING", requiredMode = Schema.RequiredMode.REQUIRED)
	private OrderStatus status;
}
