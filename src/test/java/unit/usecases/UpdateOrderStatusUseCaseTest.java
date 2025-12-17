package unit.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.application.usecases.UpdateOrderStatusUseCase;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderAlreadyHasStatusException;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderNotFoundException;
import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.OrderGateway;

import unit.fixtures.OrderFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("UpdateOrderStatusUseCase - Testes Unitários")
class UpdateOrderStatusUseCaseTest {

	@Mock
	private OrderGateway orderGateway;

	@Test @DisplayName("Deve atualizar status do pedido com sucesso")
	void shouldUpdateOrderStatusSuccessfully() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(1L);
		order.setOrderStatus(OrderStatus.RECEIVED);

		when(orderGateway.findById(1L)).thenReturn(Optional.of(order));

		// Act
		var result = UpdateOrderStatusUseCase.updateOrderStatus(1L, OrderStatus.PREPARING, orderGateway);

		// Assert
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(OrderStatus.PREPARING, result.getOrderStatus());
	}

	@Test
	@DisplayName("Deve lançar exceção quando pedido não for encontrado")
	void shouldThrowExceptionWhenOrderNotFound() {
		// Arrange
		when(orderGateway.findById(999L)).thenReturn(Optional.empty());

		// Act & Assert
		var exception = assertThrows(OrderNotFoundException.class,
				() -> UpdateOrderStatusUseCase.updateOrderStatus(999L, OrderStatus.PREPARING, orderGateway));

		assertEquals("Pedido não encontrado com id: 999", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção quando pedido já possui o status informado")
	void shouldThrowExceptionWhenOrderAlreadyHasStatus() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(2L);
		order.setOrderStatus(OrderStatus.READY);

		when(orderGateway.findById(2L)).thenReturn(Optional.of(order));

		// Act & Assert
		var exception = assertThrows(OrderAlreadyHasStatusException.class,
				() -> UpdateOrderStatusUseCase.updateOrderStatus(2L, OrderStatus.READY, orderGateway));

		assertEquals("O pedido já possui o status informado para atualização", exception.getMessage());
	}

	@Test @DisplayName("Deve atualizar de RECEIVED para PREPARING")
	void shouldUpdateFromReceivedToPreparing() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(3L);
		order.setOrderStatus(OrderStatus.RECEIVED);

		when(orderGateway.findById(3L)).thenReturn(Optional.of(order));

		// Act
		var result = UpdateOrderStatusUseCase.updateOrderStatus(3L, OrderStatus.PREPARING, orderGateway);

		// Assert
		assertNotNull(result);
		assertEquals(OrderStatus.PREPARING, result.getOrderStatus());
	}

	@Test @DisplayName("Deve atualizar de PREPARING para READY")
	void shouldUpdateFromPreparingToReady() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(4L);
		order.setOrderStatus(OrderStatus.PREPARING);

		when(orderGateway.findById(4L)).thenReturn(Optional.of(order));

		// Act
		var result = UpdateOrderStatusUseCase.updateOrderStatus(4L, OrderStatus.READY, orderGateway);

		// Assert
		assertNotNull(result);
		assertEquals(OrderStatus.READY, result.getOrderStatus());
	}

	@Test @DisplayName("Deve atualizar de READY para COMPLETED")
	void shouldUpdateFromReadyToCompleted() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(5L);
		order.setOrderStatus(OrderStatus.READY);

		when(orderGateway.findById(5L)).thenReturn(Optional.of(order));

		// Act
		var result = UpdateOrderStatusUseCase.updateOrderStatus(5L, OrderStatus.COMPLETED, orderGateway);

		// Assert
		assertNotNull(result);
		assertEquals(OrderStatus.COMPLETED, result.getOrderStatus());
	}
}
