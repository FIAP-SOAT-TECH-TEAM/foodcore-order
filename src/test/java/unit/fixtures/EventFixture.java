package unit.fixtures;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.soat.fiap.food.core.order.core.domain.events.*;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.*;

/**
 * Fixture utilitária para criação de eventos relacionados ao módulo Order,
 * utilizada exclusivamente em testes unitários.
 * <p>
 * Fornece métodos estáticos para gerar eventos do domínio e seus
 * correspondentes DTOs, permitindo cenários de teste ricos e controlados.
 */
public class EventFixture {

	/**
	 * Cria um {@link OrderCreatedEventDto} contendo um único item configurado
	 * automaticamente.
	 *
	 * @param orderId
	 *            ID do pedido
	 * @param orderNumber
	 *            número do pedido
	 * @param userId
	 *            ID do usuário criador
	 * @param totalAmount
	 *            valor total do pedido
	 * @return instância populada de {@link OrderCreatedEventDto}
	 */
	public static OrderCreatedEventDto createOrderCreatedEventDto(Long orderId, String orderNumber, String userId,
			BigDecimal totalAmount) {
		OrderItemCreatedEventDto item = new OrderItemCreatedEventDto();
		item.setId(1L);
		item.setProductId(10L);
		item.setName("Produto Teste");
		item.setQuantity(2);
		item.setUnitPrice(totalAmount.divide(BigDecimal.valueOf(2)));
		item.setSubtotal(totalAmount);
		item.setObservations("Item teste");

		OrderCreatedEventDto event = new OrderCreatedEventDto();
		event.setId(orderId);
		event.setOrderNumber(orderNumber);
		event.setStatusDescription("CRIADO");
		event.setUserId(userId);
		event.setTotalAmount(totalAmount);
		event.setItems(List.of(item));

		return event;
	}

	/**
	 * Cria um {@link OrderCanceledEventDto} contendo múltiplos itens cancelados.
	 *
	 * @param orderId
	 *            ID do pedido
	 * @param totalAmount
	 *            valor total do pedido
	 * @return instância populada de {@link OrderCanceledEventDto}
	 */
	public static OrderCanceledEventDto createOrderCanceledEventDto(Long orderId, BigDecimal totalAmount) {

		OrderItemCanceledEventDto item1 = new OrderItemCanceledEventDto();
		item1.setId(1L);
		item1.setProductId(101L);
		item1.setName("Item cancelado 1");
		item1.setQuantity(1);
		item1.setUnitPrice(totalAmount.divide(BigDecimal.valueOf(2)));
		item1.setSubtotal(item1.getUnitPrice());
		item1.setObservations("Cancelado por indisponibilidade");

		OrderItemCanceledEventDto item2 = new OrderItemCanceledEventDto();
		item2.setId(2L);
		item2.setProductId(102L);
		item2.setName("Item cancelado 2");
		item2.setQuantity(1);
		item2.setUnitPrice(totalAmount.divide(BigDecimal.valueOf(2)));
		item2.setSubtotal(item2.getUnitPrice());
		item2.setObservations("Cancelado por desistência do cliente");

		OrderCanceledEventDto event = new OrderCanceledEventDto();
		event.setId(orderId);
		event.setItems(List.of(item1, item2));

		return event;
	}

	/**
	 * Cria um {@link OrderReadyEventDto} representando um pedido pronto.
	 *
	 * @param clientId
	 *            ID do cliente
	 * @param orderNumber
	 *            número do pedido
	 * @param amount
	 *            valor total do pedido
	 * @param readyAt
	 *            data/hora em que o pedido ficou pronto
	 * @return instância populada de {@link OrderReadyEventDto}
	 */
	public static OrderReadyEventDto createOrderReadyEventDto(String clientId, String orderNumber, BigDecimal amount,
			String readyAt) {
		OrderReadyEventDto event = new OrderReadyEventDto();
		event.setClientId(clientId);
		event.setOrderNumber(orderNumber);
		event.setAmount(amount);
		event.setReadyAt(readyAt);

		return event;
	}

	/**
	 * Cria um {@link PaymentApprovedEventDto} simulando um pagamento aprovado.
	 * <p>
	 * Útil para testes de confirmação de pagamento ou atualização de status.
	 *
	 * @param paymentId
	 *            identificador único do pagamento
	 * @param orderId
	 *            identificador do pedido
	 * @param amount
	 *            valor aprovado
	 * @param paymentMethod
	 *            método utilizado (ex.: "CREDIT_CARD")
	 * @param approvedAt
	 *            data/hora da aprovação
	 * @return instância de {@link PaymentApprovedEventDto}
	 */
	public static PaymentApprovedEventDto createPaymentApprovedEventDto(UUID paymentId, Long orderId, BigDecimal amount,
			String paymentMethod, LocalDateTime approvedAt) {
		PaymentApprovedEventDto event = new PaymentApprovedEventDto();
		event.paymentId = paymentId;
		event.orderId = orderId;
		event.amount = amount;
		event.paymentMethod = paymentMethod;
		event.approvedAt = approvedAt;

		return event;
	}

	/**
	 * Cria um {@link OrderCreatedEvent} do domínio contendo um único item.
	 *
	 * @param orderId
	 *            ID do pedido
	 * @param orderNumber
	 *            número do pedido
	 * @param userId
	 *            ID do usuário criador
	 * @param totalAmount
	 *            valor total do pedido
	 * @return instância populada de {@link OrderCreatedEvent}
	 */
	public static OrderCreatedEvent createOrderCreatedEvent(Long orderId, String orderNumber, String userId,
			BigDecimal totalAmount) {
		OrderItemCreatedEvent item = new OrderItemCreatedEvent();
		item.setId(1L);
		item.setProductId(10L);
		item.setName("Produto Teste");
		item.setQuantity(2);
		item.setUnitPrice(totalAmount.divide(BigDecimal.valueOf(2)));
		item.setSubtotal(totalAmount);
		item.setObservations("Item teste");

		OrderCreatedEvent event = new OrderCreatedEvent();
		event.setId(orderId);
		event.setOrderNumber(orderNumber);
		event.setUserId(userId);
		event.setTotalAmount(totalAmount);
		event.setStatusDescription("CRIADO");
		event.setItems(List.of(item));

		return event;
	}

	/**
	 * Cria um {@link OrderCanceledEvent} do domínio contendo múltiplos itens.
	 *
	 * @param orderId
	 *            ID do pedido
	 * @param totalAmount
	 *            valor total do pedido
	 * @return instância populada de {@link OrderCanceledEvent}
	 */
	public static OrderCanceledEvent createOrderCanceledEvent(Long orderId, BigDecimal totalAmount) {

		OrderItemCanceledEvent item1 = new OrderItemCanceledEvent();
		item1.setId(1L);
		item1.setProductId(101L);
		item1.setName("Item cancelado 1");
		item1.setQuantity(1);
		item1.setUnitPrice(totalAmount.divide(BigDecimal.valueOf(2)));
		item1.setSubtotal(item1.getUnitPrice());
		item1.setObservations("Cancelado por indisponibilidade");

		OrderItemCanceledEvent item2 = new OrderItemCanceledEvent();
		item2.setId(2L);
		item2.setProductId(102L);
		item2.setName("Item cancelado 2");
		item2.setQuantity(1);
		item2.setUnitPrice(totalAmount.divide(BigDecimal.valueOf(2)));
		item2.setSubtotal(item2.getUnitPrice());
		item2.setObservations("Cancelado por desistência do cliente");

		OrderCanceledEvent event = new OrderCanceledEvent();
		event.setId(orderId);
		event.setItems(List.of(item1, item2));

		return event;
	}

	/**
	 * Cria um {@link OrderReadyEvent} representando um pedido pronto no domínio.
	 *
	 * @param clientId
	 *            ID do cliente
	 * @param orderNumber
	 *            número do pedido
	 * @param amount
	 *            valor total do pedido
	 * @return instância de {@link OrderReadyEvent}
	 */
	public static OrderReadyEvent createOrderReadyEvent(String clientId, String orderNumber, BigDecimal amount) {
		OrderReadyEvent event = new OrderReadyEvent();
		event.setClientId(clientId);
		event.setOrderNumber(orderNumber);
		event.setAmount(amount);

		return event;
	}
}
