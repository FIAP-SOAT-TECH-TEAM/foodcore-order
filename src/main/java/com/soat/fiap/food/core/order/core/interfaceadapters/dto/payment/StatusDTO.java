package com.soat.fiap.food.core.order.core.interfaceadapters.dto.payment;

import lombok.Getter;

/**
 * Enum utilizado para representar os possíveis status de um pagamento.
 */
@Getter
public enum StatusDTO {
	PENDING("Pendente"), APPROVED("Aprovado"), REJECTED("Rejeitado"), CANCELLED("Cancelado");

	private final String description;

	StatusDTO(String description) {
		this.description = description;
	}
}
