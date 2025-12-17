package unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.application.usecases.PublishOrderReadyEventUseCase;
import com.soat.fiap.food.core.order.core.domain.events.OrderReadyEvent;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.EventPublisherGateway;

import unit.fixtures.OrderFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("PublishOrderReadyEventUseCase - Testes Unitários")
class PublishOrderReadyEventUseCaseTest {

	@Mock
	private EventPublisherGateway eventPublisherGateway;

	@Test @DisplayName("Deve publicar OrderReadyEvent com sucesso")
	void shouldPublishOrderReadyEventSuccessfully() {
		// Arrange
		var order = OrderFixture.createValidOrder();

		var eventCaptor = ArgumentCaptor.forClass(OrderReadyEvent.class);

		// Act
		PublishOrderReadyEventUseCase.publishCreateOrderEvent(order, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway).publishOrderReadyEvent(eventCaptor.capture());

		var event = eventCaptor.getValue();

		assertThat(event).isNotNull();
		assertThat(event.getClientId()).isEqualTo(order.getUserId());
		assertThat(event.getOrderNumber()).isEqualTo(order.getOrderNumber());
		assertThat(event.getAmount()).isEqualTo(order.getAmount());
	}

	@Test @DisplayName("Deve preencher readyAt com timestamp formatado")
	void shouldFillReadyAtWithFormattedTimestamp() {
		// Arrange
		var order = OrderFixture.createValidOrder();

		var eventCaptor = ArgumentCaptor.forClass(OrderReadyEvent.class);

		// Act
		PublishOrderReadyEventUseCase.publishCreateOrderEvent(order, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway).publishOrderReadyEvent(eventCaptor.capture());
		var event = eventCaptor.getValue();

		assertThat(event.getReadyAt()).isNotNull();

		var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

		assertDoesNotThrow(() -> LocalDateTime.parse(event.getReadyAt(), formatter));
	}

	@Test @DisplayName("Deve publicar evento com todos os dados completos")
	void shouldPublishEventWithAllFields() {
		// Arrange
		var order = OrderFixture.createValidOrder();

		var eventCaptor = ArgumentCaptor.forClass(OrderReadyEvent.class);

		// Act
		PublishOrderReadyEventUseCase.publishCreateOrderEvent(order, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway).publishOrderReadyEvent(eventCaptor.capture());
		var event = eventCaptor.getValue();

		assertThat(event.getClientId()).isEqualTo(order.getUserId());
		assertThat(event.getOrderNumber()).isEqualTo(order.getOrderNumber());
		assertThat(event.getAmount()).isEqualTo(order.getAmount());
		assertThat(event.getReadyAt()).isNotBlank();
	}

	@Test @DisplayName("Deve executar sem lançar exceção")
	void shouldExecuteWithoutThrowingException() {
		// Arrange
		var order = OrderFixture.createValidOrder();

		// Act & Assert
		assertDoesNotThrow(() -> PublishOrderReadyEventUseCase.publishCreateOrderEvent(order, eventPublisherGateway));

		verify(eventPublisherGateway).publishOrderReadyEvent(any(OrderReadyEvent.class));
	}
}
