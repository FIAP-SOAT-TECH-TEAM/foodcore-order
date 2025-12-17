package unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.application.usecases.GetActiveOrdersSortedUseCase;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.OrderGateway;

import unit.fixtures.OrderFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("GetActiveOrdersSortedUseCase - Testes Unitários")
class GetActiveOrdersSortedUseCaseTest {

	@Mock
	private OrderGateway orderGateway;

	@Test @DisplayName("Deve buscar pedidos ativos ordenados com sucesso")
	void shouldFetchActiveOrdersSortedSuccessfully() {
		// Arrange
		var orders = List.of(OrderFixture.createValidOrder(), OrderFixture.createValidOrder());

		when(orderGateway.findActiveOrdersSorted()).thenReturn(orders);

		// Act
		var result = GetActiveOrdersSortedUseCase.getActiveOrdersSorted(orderGateway);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertThat(result).containsExactlyElementsOf(orders);

		verify(orderGateway).findActiveOrdersSorted();
	}

	@Test
	@DisplayName("Deve retornar lista vazia quando não há pedidos ativos")
	void shouldReturnEmptyListWhenNoActiveOrders() {
		// Arrange
		when(orderGateway.findActiveOrdersSorted()).thenReturn(List.of());

		// Act
		var result = GetActiveOrdersSortedUseCase.getActiveOrdersSorted(orderGateway);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result).isEmpty();

		verify(orderGateway).findActiveOrdersSorted();
	}

	@Test
	@DisplayName("Deve chamar o gateway apenas uma vez")
	void shouldCallGatewayOnlyOnce() {
		// Arrange
		when(orderGateway.findActiveOrdersSorted()).thenReturn(List.of());

		// Act
		GetActiveOrdersSortedUseCase.getActiveOrdersSorted(orderGateway);

		// Assert
		verify(orderGateway, times(1)).findActiveOrdersSorted();
	}
}
