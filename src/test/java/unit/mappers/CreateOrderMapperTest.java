package unit.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.order.core.application.inputs.CreateOrderInput;
import com.soat.fiap.food.core.order.core.application.inputs.mappers.CreateOrderMapper;
import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.CreateOrderRequest;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.OrderItemRequest;
import com.soat.fiap.food.core.shared.core.interfaceadapters.gateways.AuthenticatedUserGateway;

import unit.fixtures.OrderFixture;

@DisplayName("CreateOrderMapper - Testes Unitários")
class CreateOrderMapperTest {

	@Test @DisplayName("Deve mapear CreateOrderRequest para CreateOrderInput com sucesso")
	void shouldMapCreateOrderRequestToInput() {
		// Arrange
		var itemRequest = new OrderItemRequest(1L, "Big Mac", 2, new BigDecimal("25.90"), null);
		var request = new CreateOrderRequest(List.of(itemRequest));

		var authenticatedUserGateway = mock(AuthenticatedUserGateway.class);
		when(authenticatedUserGateway.getSubject()).thenReturn("user-123");

		// Act
		var input = assertDoesNotThrow(() -> CreateOrderMapper.toInput(request, authenticatedUserGateway));

		// Assert
		assertThat(input).isNotNull();
		assertThat(input.userId()).isEqualTo("user-123");
		assertThat(input.items()).hasSize(1);
		assertThat(input.items().getFirst().name()).isEqualTo("Big Mac");
		assertThat(input.items().getFirst().quantity()).isEqualTo(2);
	}

	@Test @DisplayName("Deve mapear CreateOrderInput para Order (domínio) com sucesso")
	void shouldMapCreateOrderInputToDomain() {
		// Arrange
		CreateOrderInput input = OrderFixture.createValidCreateOrderInput();

		// Act
		Order order = assertDoesNotThrow(() -> CreateOrderMapper.toDomain(input));

		// Assert
		assertThat(order).isNotNull();
		assertThat(order.getUserId()).isEqualTo(input.userId());
		assertThat(order.getOrderItems()).hasSize(1);
		assertThat(order.getOrderItems().getFirst().getName()).isEqualTo("Big Mac");
		assertThat(order.getOrderItems().getFirst().getUnitPrice()).isEqualByComparingTo("25.90");
	}

	@Test @DisplayName("Deve mapear CreateOrderInput com múltiplos itens para Order (domínio)")
	void shouldMapCreateOrderInputWithMultipleItemsToDomain() {
		// Arrange
		CreateOrderInput input = OrderFixture.createCreateOrderInputWithMultipleItems();

		// Act
		Order order = assertDoesNotThrow(() -> CreateOrderMapper.toDomain(input));

		// Assert
		assertThat(order).isNotNull();
		assertThat(order.getOrderItems()).hasSize(3);
		assertThat(order.getOrderItems().get(1).getName()).isEqualTo("Coca-Cola");
		assertThat(order.getOrderItems().get(2).getName()).isEqualTo("Torta de Chocolate");
	}
}
