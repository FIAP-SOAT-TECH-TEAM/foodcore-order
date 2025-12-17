package com.soat.fiap.food.core.order.core.domain.vo;

import lombok.Getter;

/**
 * Enum que representa os possíveis status de um pedido
 */
@Getter
public enum OrderStatus {

	RECEIVED(1, "Recebido"), PREPARING(2, "Em Preparação"), READY(3, "Pronto"), COMPLETED(4, "Finalizado"), CANCELLED(5,
			"Cancelado");

	private final int code;
	private final String description;

	OrderStatus(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public static OrderStatus fromCode(int code) {
		for (OrderStatus status : values()) {
			if (status.code == code) {
				return status;
			}
		}
		throw new IllegalArgumentException("Código inválido para OrderStatus: " + code);
	}
}
