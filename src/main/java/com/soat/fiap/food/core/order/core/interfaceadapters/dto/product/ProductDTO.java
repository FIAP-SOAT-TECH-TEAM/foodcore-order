package com.soat.fiap.food.core.order.core.interfaceadapters.dto.product;

import java.math.BigDecimal;

/**
 * DTO utilizado para representar dados da entidade Product. Serve como objeto
 * de transferência entre o domínio e o mundo externo (DataSource).
 *
 * @param id
 *            Identificador único do produto.
 * @param name
 *            Nome do produto.
 * @param price
 *            Preço do produto.
 * @param stock
 *            Informações de estoque do produto.
 * @param active
 *            Indica se o produto está ativo.
 * @param categoryIsActive
 *            Indica se a categoria do produto está ativa.
 */
public record ProductDTO(Long id, String name, BigDecimal price, boolean active, boolean categoryIsActive,
		StockDTO stock) {
}
