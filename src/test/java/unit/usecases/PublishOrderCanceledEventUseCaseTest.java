package unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.application.usecases.PublishOrderCanceledEventUseCase;
import com.soat.fiap.food.core.order.core.domain.events.OrderCanceledEvent;
import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.EventPublisherGateway;

import unit.fixtures.OrderFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("PublishOrderCanceledEventUseCase - Testes Unitários")
class PublishOrderCanceledEventUseCaseTest {

	@Mock
	private EventPublisherGateway eventPublisherGateway;

	@Test @DisplayName("Deve publicar evento de pedido cancelado com sucesso")
	void shouldPublishOrderCanceledEventSuccessfully() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(1L);
		order.setOrderStatus(OrderStatus.CANCELLED);

		var eventCaptor = ArgumentCaptor.forClass(OrderCanceledEvent.class);

		// Act
		PublishOrderCanceledEventUseCase.publishOrderCanceledEvent(order, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway).publishOrderCanceledEvent(eventCaptor.capture());

		var publishedEvent = eventCaptor.getValue();
		assertThat(publishedEvent).isNotNull();
		assertThat(publishedEvent.getId()).isEqualTo(1L);
		assertThat(publishedEvent.getStatus()).isEqualTo(OrderStatus.CANCELLED);
	}

	@Test @DisplayName("Deve publicar evento com itens do pedido cancelado")
	void shouldPublishEventWithCanceledOrderItems() {
		// Arrange
		var order = OrderFixture.createOrderWithMultipleItems();
		order.setId(2L);
		order.setOrderStatus(OrderStatus.CANCELLED);

		var eventCaptor = ArgumentCaptor.forClass(OrderCanceledEvent.class);

		// Act
		PublishOrderCanceledEventUseCase.publishOrderCanceledEvent(order, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway).publishOrderCanceledEvent(eventCaptor.capture());

		var publishedEvent = eventCaptor.getValue();
		assertThat(publishedEvent).isNotNull();
		assertThat(publishedEvent.getId()).isEqualTo(2L);
		assertThat(publishedEvent.getItems()).isNotEmpty();
		assertThat(publishedEvent.getItems()).hasSize(order.getOrderItems().size());
	}

	@Test @DisplayName("Deve publicar evento com dados completos do pedido cancelado")
	void shouldPublishEventWithCompleteCanceledOrderData() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(3L);
		order.setOrderStatus(OrderStatus.CANCELLED);

		var eventCaptor = ArgumentCaptor.forClass(OrderCanceledEvent.class);

		// Act
		PublishOrderCanceledEventUseCase.publishOrderCanceledEvent(order, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway).publishOrderCanceledEvent(eventCaptor.capture());

		var publishedEvent = eventCaptor.getValue();
		assertThat(publishedEvent).isNotNull();
		assertThat(publishedEvent.getId()).isEqualTo(3L);
		assertThat(publishedEvent.getStatus()).isEqualTo(OrderStatus.CANCELLED);
	}
}
