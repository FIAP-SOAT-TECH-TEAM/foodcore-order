package unit.gateway;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderCanceledEventDto;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderReadyEventDto;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.EventPublisherGateway;
import com.soat.fiap.food.core.order.infrastructure.common.source.EventPublisherSource;

import unit.fixtures.EventFixture;

/**
 * Testes unitários para {@link EventPublisherGateway}.
 * <p>
 * Valida a delegação correta para {@link EventPublisherSource} ao publicar
 * eventos de pedido criados, cancelados e prontos.
 * </p>
 */
@ExtendWith(MockitoExtension.class) @DisplayName("EventPublisherGateway - Testes Unitários")
class EventPublisherGatewayTest {

	@Mock
	private EventPublisherSource eventPublisherSource;

	@Test @DisplayName("Deve publicar evento OrderCreatedEvent com sucesso")
	void shouldPublishOrderCreatedEventSuccessfully() {
		// Arrange
		var gateway = new EventPublisherGateway(eventPublisherSource);
		var event = EventFixture.createOrderCreatedEvent(1L, "ORD-001", "user-1", BigDecimal.valueOf(200.00));

		// Act & Assert
		assertThatNoException().isThrownBy(() -> gateway.publishOrderCreatedEvent(event));
		verify(eventPublisherSource).publishOrderCreatedEvent(any(OrderCreatedEventDto.class));
	}

	@Test @DisplayName("Deve publicar evento OrderCanceledEvent com sucesso")
	void shouldPublishOrderCanceledEventSuccessfully() {
		// Arrange
		var gateway = new EventPublisherGateway(eventPublisherSource);
		var event = EventFixture.createOrderCanceledEvent(10L, BigDecimal.valueOf(120.00));

		// Act & Assert
		assertThatNoException().isThrownBy(() -> gateway.publishOrderCanceledEvent(event));
		verify(eventPublisherSource).publishOrderCanceledEvent(any(OrderCanceledEventDto.class));
	}

	@Test @DisplayName("Deve publicar evento OrderReadyEvent com sucesso")
	void shouldPublishOrderReadyEventSuccessfully() {
		// Arrange
		var gateway = new EventPublisherGateway(eventPublisherSource);
		var event = EventFixture.createOrderReadyEvent("client-1", "ORD-999", BigDecimal.valueOf(150.00));

		// Act & Assert
		assertThatNoException().isThrownBy(() -> gateway.publishOrderReadyEvent(event));
		verify(eventPublisherSource).publishOrderReadyEvent(any(OrderReadyEventDto.class));
	}

	@Test @DisplayName("Deve delegar corretamente múltiplos eventos para EventPublisherSource")
	void shouldDelegateCorrectlyForMultipleEvents() {
		// Arrange
		var gateway = new EventPublisherGateway(eventPublisherSource);

		var createdEvent = EventFixture.createOrderCreatedEvent(100L, "ORD-100", "user-100",
				BigDecimal.valueOf(300.00));
		var canceledEvent = EventFixture.createOrderCanceledEvent(200L, BigDecimal.valueOf(400.00));
		var readyEvent = EventFixture.createOrderReadyEvent("client-200", "ORD-200", BigDecimal.valueOf(500.00));

		// Act
		gateway.publishOrderCreatedEvent(createdEvent);
		gateway.publishOrderCanceledEvent(canceledEvent);
		gateway.publishOrderReadyEvent(readyEvent);

		// Assert
		verify(eventPublisherSource).publishOrderCreatedEvent(any(OrderCreatedEventDto.class));
		verify(eventPublisherSource).publishOrderCanceledEvent(any(OrderCanceledEventDto.class));
		verify(eventPublisherSource).publishOrderReadyEvent(any(OrderReadyEventDto.class));
	}
}
