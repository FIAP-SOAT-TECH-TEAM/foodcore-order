package com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.response;

import java.time.LocalDateTime;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta após atualização do status de um pedido
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Resposta de atualização do status de um pedido")
public class OrderStatusResponse {
	@Schema(description = "ID do pedido")
	private Long orderId;
	@Schema(description = "Status do pedido")
	private OrderStatus orderStatus;
	@Schema(description = "Data e hora da criação do pedido")
	private LocalDateTime createdAt;
	@Schema(description = "Data e hora da atualização do status")
	private LocalDateTime updatedAt;
}
