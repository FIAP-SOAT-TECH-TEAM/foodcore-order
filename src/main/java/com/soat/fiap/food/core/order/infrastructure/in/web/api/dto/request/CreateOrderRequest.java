package com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para criação de um novo pedido
 */
@Data @NoArgsConstructor @AllArgsConstructor @Schema(description = "Requisição para criação de um novo pedido")
public class CreateOrderRequest {

	@NotEmpty(message = "A lista de itens não pode estar vazia") @Valid
	@Schema(description = "Lista de itens do pedido", requiredMode = Schema.RequiredMode.REQUIRED)
	private List<OrderItemRequest> items;
}
