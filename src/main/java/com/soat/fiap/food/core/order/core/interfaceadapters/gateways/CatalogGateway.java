package com.soat.fiap.food.core.order.core.interfaceadapters.gateways;

import java.util.List;

import com.soat.fiap.food.core.order.core.interfaceadapters.dto.product.ProductDTO;
import com.soat.fiap.food.core.order.infrastructure.common.source.CatalogDataSource;

/**
 * Gateway para comunicação com o microsserviço de catálogo
 */
public class CatalogGateway {

	private final CatalogDataSource catalogDatasource;

	public CatalogGateway(CatalogDataSource catalogDatasource) {
		this.catalogDatasource = catalogDatasource;
	}

	/**
	 * Retorna uma lista de Produtos por IDs
	 *
	 * @param productIds
	 *            IDs do produtos
	 */
	public List<ProductDTO> findByProductIds(List<Long> productIds) {
		return catalogDatasource.findByProductIds(productIds);
	}

}
