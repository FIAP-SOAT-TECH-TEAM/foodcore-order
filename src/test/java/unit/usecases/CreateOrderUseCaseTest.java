package unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.application.usecases.CreateOrderUseCase;

import unit.fixtures.OrderFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("CreateOrderUseCase - Testes Unitários")
class CreateOrderUseCaseTest {

	@Test @DisplayName("Deve criar pedido com sucesso")
	void shouldCreateOrderSuccessfully() {
		// Arrange
		var createOrderInput = OrderFixture.createValidCreateOrderInput();

		// Act
		var result = CreateOrderUseCase.createOrder(createOrderInput);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getUserId()).isEqualTo(createOrderInput.userId());
		assertThat(result.getAmount()).isPositive();
	}

	@Test @DisplayName("Deve executar criação sem exceções")
	void shouldExecuteCreationWithoutExceptions() {
		// Arrange
		var createOrderInput = OrderFixture.createValidCreateOrderInput();

		// Act & Assert
		assertDoesNotThrow(() -> CreateOrderUseCase.createOrder(createOrderInput));
	}

	@Test @DisplayName("Deve retornar pedido válido após criação")
	void shouldReturnValidOrderAfterCreation() {
		// Arrange
		var createOrderInput = OrderFixture.createValidCreateOrderInput();

		// Act
		var result = CreateOrderUseCase.createOrder(createOrderInput);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getOrderItems()).isNotEmpty();
		assertThat(result.getCreatedAt()).isNotNull();
	}
}
