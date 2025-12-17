package com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar um item de pedido na requisição
 */
@Data @NoArgsConstructor @AllArgsConstructor @Schema(description = "Item do pedido")
public class OrderItemRequest {

	@NotNull(message = "O ID do produto é obrigatório") @Positive(message = "O ID do produto deve ser positivo") @Schema(description = "ID do produto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long productId;

	@NotEmpty(message = "O nome do produto do item do pedido é obrigatório")
	@Schema(description = "Nome do produto do item do pedido", example = "X-Burger", requiredMode = Schema.RequiredMode.REQUIRED)
	private String name = "";

	@NotNull(message = "A quantidade é obrigatória") @Positive(message = "A quantidade deve ser positiva") @Schema(description = "Quantidade do produto", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
	private Integer quantity;

	@NotNull(message = "O preço unitário é obrigatório") @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero")
	@Schema(description = "Preço unitário do produto", example = "22.90", requiredMode = Schema.RequiredMode.REQUIRED)
	private BigDecimal unitPrice;

	@Schema(description = "Observações adicionais sobre o item", example = "Sem cebola")
	private String observations = "";
}
