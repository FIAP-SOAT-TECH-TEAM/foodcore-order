package com.soat.fiap.food.core.order.core.application.usecases;

import com.soat.fiap.food.core.order.core.application.inputs.CreateOrderInput;
import com.soat.fiap.food.core.order.core.application.inputs.mappers.CreateOrderMapper;
import com.soat.fiap.food.core.order.core.domain.model.Order;

import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso: Criar pedido.
 */
@Slf4j
public class CreateOrderUseCase {

	/**
	 * Cria um novo pedido
	 *
	 * @param createOrderInput
	 *            dados de criação do pedido
	 * @return Pedido criado
	 */
	public static Order createOrder(CreateOrderInput createOrderInput) {

		log.info("Criando novo pedido para o cliente ID: {}", createOrderInput.userId());

		return CreateOrderMapper.toDomain(createOrderInput);
	}
}
