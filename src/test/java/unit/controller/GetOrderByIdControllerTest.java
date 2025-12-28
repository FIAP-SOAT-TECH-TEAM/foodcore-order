package unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.interfaceadapters.controller.GetOrderByIdController;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.mappers.OrderDTOMapper;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.response.OrderResponse;

import unit.fixtures.OrderFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("GetOrderByIdController - Testes Unitários")
class GetOrderByIdControllerTest {

	@Mock
	private OrderDataSource orderDataSource;

	@Test @DisplayName("Deve retornar pedido com sucesso quando ID existir")
	void shouldReturnOrderSuccessfullyWhenIdExists() {
		// Arrange
		Long orderId = 1L;
		var order = OrderFixture.createValidOrder();
		order.setId(orderId);
		var orderDto = Optional.of(OrderDTOMapper.toDTO(order));

		when(orderDataSource.findById(orderId)).thenReturn(orderDto);

		// Act
		var response = assertDoesNotThrow(() -> GetOrderByIdController.getOrderById(orderId, orderDataSource));

		// Assert
		assertThat(response).isNotNull();
		assertThat(response).isInstanceOf(OrderResponse.class);
		assertThat(response.getUserId()).isEqualTo(order.getUserId());
		assertThat(response.getItems()).hasSize(order.getOrderItems().size());

		verify(orderDataSource).findById(orderId);
	}

	@Test @DisplayName("Deve executar busca sem lançar exceção")
	void shouldExecuteSearchWithoutThrowingException() {
		// Arrange
		Long orderId = 2L;
		var order = OrderFixture.createOrderWithMultipleItems();
		order.setId(orderId);
		var orderDto = Optional.of(OrderDTOMapper.toDTO(order));

		when(orderDataSource.findById(orderId)).thenReturn(orderDto);

		// Act & Assert
		assertDoesNotThrow(() -> GetOrderByIdController.getOrderById(orderId, orderDataSource));

		verify(orderDataSource).findById(orderId);
	}

	@Test @DisplayName("Deve chamar o datasource corretamente ao buscar pedido")
	void shouldCallDataSourceWhenSearchingOrder() {
		// Arrange
		Long orderId = 3L;
		var order = OrderFixture.createOrderWithSingleItem();
		order.setId(orderId);
		var orderDto = Optional.of(OrderDTOMapper.toDTO(order));

		when(orderDataSource.findById(orderId)).thenReturn(orderDto);

		// Act
		GetOrderByIdController.getOrderById(orderId, orderDataSource);

		// Assert
		verify(orderDataSource).findById(orderId);
	}
}
