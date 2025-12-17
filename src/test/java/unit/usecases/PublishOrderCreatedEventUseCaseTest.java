package unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.application.usecases.PublishOrderCreatedEventUseCase;
import com.soat.fiap.food.core.order.core.domain.events.OrderCreatedEvent;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.EventPublisherGateway;

import unit.fixtures.OrderFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("PublishCreateOrderEventUseCase - Testes Unitários")
class PublishOrderCreatedEventUseCaseTest {

	@Mock
	private EventPublisherGateway eventPublisherGateway;

	@Test @DisplayName("Deve publicar evento criação pedido com sucesso")
	void shouldPublishCreateOrderEventSuccessfully() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(1L);

		var eventCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);

		// Act
		PublishOrderCreatedEventUseCase.publishCreateOrderEvent(order, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway).publishOrderCreatedEvent(eventCaptor.capture());

		var publishedEvent = eventCaptor.getValue();
		assertThat(publishedEvent).isNotNull();
		assertThat(publishedEvent.getId()).isEqualTo(1L);
		assertThat(publishedEvent.getUserId()).isEqualTo(order.getUserId());
		assertThat(publishedEvent.getStatus()).isEqualTo(order.getOrderStatus());
	}

	@Test @DisplayName("Deve publicar evento com itens do pedido")
	void shouldPublishEventWithOrderItems() {
		// Arrange
		var order = OrderFixture.createOrderWithMultipleItems();
		order.setId(2L);

		var eventCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);

		// Act
		PublishOrderCreatedEventUseCase.publishCreateOrderEvent(order, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway).publishOrderCreatedEvent(eventCaptor.capture());

		var publishedEvent = eventCaptor.getValue();
		assertThat(publishedEvent).isNotNull();
		assertThat(publishedEvent.getId()).isEqualTo(2L);
		assertThat(publishedEvent.getItems()).isNotEmpty();
		assertThat(publishedEvent.getItems()).hasSize(order.getOrderItems().size());
	}

	@Test @DisplayName("Deve publicar evento com dados completos do pedido")
	void shouldPublishEventWithCompleteOrderData() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(3L);

		var eventCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);

		// Act
		PublishOrderCreatedEventUseCase.publishCreateOrderEvent(order, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway).publishOrderCreatedEvent(eventCaptor.capture());

		var publishedEvent = eventCaptor.getValue();
		assertThat(publishedEvent).isNotNull();
		assertThat(publishedEvent.getId()).isEqualTo(3L);
		assertThat(publishedEvent.getTotalAmount()).isEqualTo(order.getAmount());
		assertThat(publishedEvent.getOrderNumber()).isEqualTo(order.getOrderNumber());
	}

	@Test @DisplayName("Deve publicar evento de criação de pedido com sucesso")
	void shouldPublishOrderCreationEventSuccessfully() {
		// Arrange
		var order = OrderFixture.createValidOrder();

		// Act
		assertDoesNotThrow(() -> PublishOrderCreatedEventUseCase.publishCreateOrderEvent(order, eventPublisherGateway));

		// Assert
		verify(eventPublisherGateway).publishOrderCreatedEvent(any());
	}

	@Test @DisplayName("Deve executar publicação sem lançar exceção")
	void shouldExecutePublicationWithoutThrowingException() {
		// Arrange
		var order = OrderFixture.createOrderWithMultipleItems();

		// Act & Assert
		assertDoesNotThrow(() -> PublishOrderCreatedEventUseCase.publishCreateOrderEvent(order, eventPublisherGateway));

		verify(eventPublisherGateway).publishOrderCreatedEvent(any());
	}
}
