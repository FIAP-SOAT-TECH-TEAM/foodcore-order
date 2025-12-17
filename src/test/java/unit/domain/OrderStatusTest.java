package unit.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;

@DisplayName("OrderStatus - Testes de Domínio")
class OrderStatusTest {

	@Test @DisplayName("Deve retornar o status correto a partir do código")
	void shouldReturnCorrectStatusFromCode() {
		// Arrange & Act & Assert
		assertEquals(OrderStatus.RECEIVED, OrderStatus.fromCode(1));
		assertEquals(OrderStatus.PREPARING, OrderStatus.fromCode(2));
		assertEquals(OrderStatus.READY, OrderStatus.fromCode(3));
		assertEquals(OrderStatus.COMPLETED, OrderStatus.fromCode(4));
		assertEquals(OrderStatus.CANCELLED, OrderStatus.fromCode(5));
	}

	@Test @DisplayName("Deve lançar exceção ao receber código inválido")
	void shouldThrowExceptionForInvalidCode() {
		// Arrange
		int invalidCode = 999;

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> OrderStatus.fromCode(invalidCode));

		// Assert
		assertEquals("Código inválido para OrderStatus: 999", exception.getMessage());
	}

	@Test @DisplayName("Deve conter código e descrição corretos para cada status")
	void shouldHaveCorrectCodeAndDescription() {

		// Arrange & Act & Assert
		assertAll(() -> {
			assertEquals(1, OrderStatus.RECEIVED.getCode());
			assertEquals("Recebido", OrderStatus.RECEIVED.getDescription());
		}, () -> {
			assertEquals(2, OrderStatus.PREPARING.getCode());
			assertEquals("Em Preparação", OrderStatus.PREPARING.getDescription());
		}, () -> {
			assertEquals(3, OrderStatus.READY.getCode());
			assertEquals("Pronto", OrderStatus.READY.getDescription());
		}, () -> {
			assertEquals(4, OrderStatus.COMPLETED.getCode());
			assertEquals("Finalizado", OrderStatus.COMPLETED.getDescription());
		}, () -> {
			assertEquals(5, OrderStatus.CANCELLED.getCode());
			assertEquals("Cancelado", OrderStatus.CANCELLED.getDescription());
		});
	}

	@Test @DisplayName("Deve permitir iterar todos os valores e garantir que não sejam nulos")
	void shouldIterateAllValues() {
		// Arrange & Act & Assert
		for (OrderStatus status : OrderStatus.values()) {
			assertNotNull(status);
			assertNotNull(status.getDescription());
		}
	}
}
