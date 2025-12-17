package unit.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.application.usecases.GetOrderByIdUseCase;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderNotFoundException;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.OrderGateway;

import unit.fixtures.OrderFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("GetOrderByIdUseCase - Testes Unitários")
class GetOrderByIdUseCaseTest {

	@Mock
	private OrderGateway orderGateway;

	@Test @DisplayName("Deve retornar pedido quando encontrado por ID")
	void shouldReturnOrderWhenFoundById() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(1L);
		when(orderGateway.findById(1L)).thenReturn(Optional.of(order));

		// Act
		var result = GetOrderByIdUseCase.getOrderById(1L, orderGateway);

		// Assert
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(order.getUserId(), result.getUserId());
		assertEquals(order.getOrderStatus(), result.getOrderStatus());
	}

	@Test
	@DisplayName("Deve lançar exceção quando pedido não for encontrado")
	void shouldThrowExceptionWhenOrderNotFound() {
		// Arrange
		when(orderGateway.findById(999L)).thenReturn(Optional.empty());

		// Act & Assert
		var exception = assertThrows(OrderNotFoundException.class,
				() -> GetOrderByIdUseCase.getOrderById(999L, orderGateway));

		assertEquals("Pedido não encontrado com id: 999", exception.getMessage());
	}

	@Test @DisplayName("Deve retornar pedido com itens quando encontrado")
	void shouldReturnOrderWithItemsWhenFound() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(2L);
		when(orderGateway.findById(2L)).thenReturn(Optional.of(order));

		// Act
		var result = GetOrderByIdUseCase.getOrderById(2L, orderGateway);

		// Assert
		assertNotNull(result);
		assertEquals(2L, result.getId());
		assertFalse(result.getOrderItems().isEmpty());
		assertEquals(1, result.getOrderItems().size());
	}
}
