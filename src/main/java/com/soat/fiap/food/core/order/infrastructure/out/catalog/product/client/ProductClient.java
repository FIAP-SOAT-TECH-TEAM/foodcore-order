package com.soat.fiap.food.core.order.infrastructure.out.catalog.product.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soat.fiap.food.core.order.infrastructure.out.catalog.product.entity.ProductEntity;

/**
 * Cliente de conexão com a API do microsserviço de Catalog (Product)
 */
@FeignClient(name = "${catalog.service.name}", url = "${catalog.service.url}")
public interface ProductClient {

	@GetMapping("/catalogs/categories/products")
	ResponseEntity<List<ProductEntity>> getProductsByIds(@RequestParam List<Long> productIds);
}
