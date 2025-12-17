package com.soat.fiap.food.core.order.core.interfaceadapters.dto.product;

/**
 * DTO utilizado para representar dados da entidade Stock. Serve como objeto de
 * transferência entre o domínio e o mundo externo (DataSource).
 *
 * @param quantity
 *            Quantidade disponível em estoque.
 */
public record StockDTO(Integer quantity) {
}
