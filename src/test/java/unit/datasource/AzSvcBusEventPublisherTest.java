package unit.datasource;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.google.gson.Gson;
import com.soat.fiap.food.core.order.infrastructure.out.event.publisher.azsvcbus.AzSvcBusEventPublisher;

import unit.fixtures.EventFixture;

/**
 * Testes unitários para {@link AzSvcBusEventPublisher}.
 * <p>
 * Valida a publicação de eventos de pedido criado, cancelado e pronto no Azure
 * Service Bus sem lançar exceções.
 * </p>
 */
@ExtendWith(MockitoExtension.class) @DisplayName("AzSvcBusEventPublisher (Order) - Testes Unitários")
class AzSvcBusEventPublisherTest {

	@Mock
	private ServiceBusSenderClient orderCreatedSender;

	@Mock
	private ServiceBusSenderClient orderCanceledSender;

	@Mock
	private ServiceBusSenderClient orderReadySender;

	@Mock
	private Gson gson;

	private AzSvcBusEventPublisher eventPublisher;

	@BeforeEach
	void setUp() {
		eventPublisher = new AzSvcBusEventPublisher(orderCreatedSender, orderCanceledSender, orderReadySender, gson);
	}

	@Test @DisplayName("Deve publicar evento de pedido criado com sucesso")
	void shouldPublishOrderCreatedEventSuccessfully() {
		// Arrange
		var event = EventFixture.createOrderCreatedEventDto(1L, "ORD-123", "USR-001", new BigDecimal("100.00"));
		when(gson.toJson(event)).thenReturn("{\"orderNumber\":\"ORD-123\",\"totalAmount\":100.00}");

		// Act & Assert
		assertThatNoException().isThrownBy(() -> eventPublisher.publishOrderCreatedEvent(event));

		// Assert
		verify(orderCreatedSender).sendMessage(any(ServiceBusMessage.class));
	}

	@Test @DisplayName("Deve publicar evento de pedido cancelado com sucesso")
	void shouldPublishOrderCanceledEventSuccessfully() {
		// Arrange
		var event = EventFixture.createOrderCanceledEventDto(2L, new BigDecimal("50.00"));
		when(gson.toJson(event)).thenReturn("{\"orderId\":2,\"status\":\"CANCELADO\"}");

		// Act & Assert
		assertThatNoException().isThrownBy(() -> eventPublisher.publishOrderCanceledEvent(event));

		// Assert
		verify(orderCanceledSender).sendMessage(any(ServiceBusMessage.class));
	}

	@Test @DisplayName("Deve publicar evento de pedido pronto com sucesso")
	void shouldPublishOrderReadyEventSuccessfully() {
		// Arrange
		var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		var now = LocalDateTime.now().format(formatter);
		var event = EventFixture.createOrderReadyEventDto("USR-ABC", "ORD-456", new BigDecimal("120.00"), now);
		when(gson.toJson(event)).thenReturn(String.format(
				"{\"clientId\":\"USR-ABC\",\"orderNumber\":\"ORD-456\",\"amount\":120.00,\"readyAt\":\"%s\"}", now));

		// Act & Assert
		assertThatNoException().isThrownBy(() -> eventPublisher.publishOrderReadyEvent(event));

		// Assert
		verify(orderReadySender).sendMessage(any(ServiceBusMessage.class));
	}

	@Test @DisplayName("Deve publicar múltiplos eventos com sucesso")
	void shouldPublishMultipleEventsSuccessfully() {
		// Arrange
		var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		var createdEvent = EventFixture.createOrderCreatedEventDto(10L, "ORD-999", "USR-XYZ", new BigDecimal("200.00"));
		var canceledEvent = EventFixture.createOrderCanceledEventDto(11L, new BigDecimal("80.00"));
		var now = LocalDateTime.now().format(formatter);
		var readyEvent = EventFixture.createOrderReadyEventDto("USR-QWE", "ORD-777", new BigDecimal("300.00"), now);

		when(gson.toJson(readyEvent)).thenReturn(String.format(
				"{\"clientId\":\"USR-QWE\",\"orderNumber\":\"ORD-777\",\"amount\":300.00,\"readyAt\":\"%s\"}", now));
		when(gson.toJson(createdEvent)).thenReturn("{\"orderNumber\":\"ORD-999\",\"totalAmount\":200.00}");
		when(gson.toJson(canceledEvent)).thenReturn("{\"orderId\":11,\"status\":\"CANCELADO\"}");
		when(gson.toJson(readyEvent)).thenReturn("{\"orderId\":12,\"ready\":true}");

		// Act & Assert
		assertThatNoException().isThrownBy(() -> {
			eventPublisher.publishOrderCreatedEvent(createdEvent);
			eventPublisher.publishOrderCanceledEvent(canceledEvent);
			eventPublisher.publishOrderReadyEvent(readyEvent);
		});

		// Assert
		verify(orderCreatedSender).sendMessage(any(ServiceBusMessage.class));
		verify(orderCanceledSender).sendMessage(any(ServiceBusMessage.class));
		verify(orderReadySender).sendMessage(any(ServiceBusMessage.class));
	}
}
