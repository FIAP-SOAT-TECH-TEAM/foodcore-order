package com.soat.fiap.food.core.order.infrastructure.out.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.soat.fiap.food.core.order.infrastructure.out.payment.entity.PaymentStatusEntity;

/**
 * Cliente de conexão com a API do microsserviço de Pagamento
 */
@FeignClient(name = "${payment.service.name}", url = "${payment.service.url}")
public interface PaymentClient {

	@GetMapping("/payments/{orderId}/latest")
	ResponseEntity<PaymentStatusEntity> getOrderStatus(@PathVariable Long orderId);
}
