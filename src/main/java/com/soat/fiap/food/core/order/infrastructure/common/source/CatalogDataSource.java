package com.soat.fiap.food.core.order.infrastructure.common.source;

import java.util.List;

import com.soat.fiap.food.core.order.core.interfaceadapters.dto.product.ProductDTO;

/**
 * DataSource para obtenção de Catálogo.
 */
public interface CatalogDataSource {

	/**
	 * Retorna uma lista de Produtos por IDs
	 *
	 * @param productIds
	 *            IDs do produtos
	 */
	List<ProductDTO> findByProductIds(List<Long> productIds);

}
