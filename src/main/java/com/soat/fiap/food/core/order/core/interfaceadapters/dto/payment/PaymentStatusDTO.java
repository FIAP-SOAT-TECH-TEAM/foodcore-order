package com.soat.fiap.food.core.order.core.interfaceadapters.dto.payment;

/**
 * DTO utilizado para representar o status de um pagamento associado a um
 * pedido. Serve como objeto de transferência entre o domínio e o mundo externo
 * (DataSource).
 *
 * @param orderId
 *            Identificador único do pedido.
 * @param status
 *            Status atual do pagamento.
 */
public record PaymentStatusDTO(Long orderId, StatusDTO status) {
}
