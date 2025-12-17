package unit.vo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.order.core.domain.vo.OrderNumber;

@DisplayName("OrderNumber - Testes de Value Object")
class OrderNumberTest {

	@Test @DisplayName("Deve criar OrderNumber válido")
	void shouldCreateValidOrderNumber() {
		// Arrange
		int year = 2024;
		long sequential = 42;

		// Act
		OrderNumber orderNumber = new OrderNumber(year, sequential);

		// Assert
		assertNotNull(orderNumber);
		assertEquals(year, orderNumber.year());
		assertEquals(sequential, orderNumber.sequential());
	}

	@Test @DisplayName("Deve formatar número do pedido corretamente")
	void shouldFormatOrderNumberCorrectly() {
		// Arrange
		int year = 2024;
		long sequential = 123;
		OrderNumber orderNumber = new OrderNumber(year, sequential);

		// Act
		String formatted = orderNumber.getFormatted();

		// Assert
		assertEquals("ORD-2024-00123", formatted);
	}

	@Test @DisplayName("Deve formatar com zero à esquerda no sequencial")
	void shouldFormatWithLeadingZerosInSequential() {
		// Arrange
		int year = 2023;
		long sequential = 5;
		OrderNumber orderNumber = new OrderNumber(year, sequential);

		// Act
		String formatted = orderNumber.getFormatted();

		// Assert
		assertEquals("ORD-2023-00005", formatted);
	}

	@Test @DisplayName("Deve formatar com sequencial máximo")
	void shouldFormatWithMaxSequential() {
		// Arrange
		int year = 2025;
		long sequential = 99999;
		OrderNumber orderNumber = new OrderNumber(year, sequential);

		// Act
		String formatted = orderNumber.getFormatted();

		// Assert
		assertEquals("ORD-2025-99999", formatted);
	}

	@Test @DisplayName("Deve lançar exceção para ano inválido - muito pequeno")
	void shouldThrowExceptionForInvalidYearTooSmall() {
		// Arrange
		int year = 999;
		long sequential = 1;

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			new OrderNumber(year, sequential);
		});

		assertEquals("Ano deve ter 4 dígitos", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção para ano inválido - muito grande")
	void shouldThrowExceptionForInvalidYearTooLarge() {
		// Arrange
		int year = 10000;
		long sequential = 1;

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			new OrderNumber(year, sequential);
		});

		assertEquals("Ano deve ter 4 dígitos", exception.getMessage());
	}

	@Test @DisplayName("Deve aceitar ano mínimo válido")
	void shouldAcceptMinimumValidYear() {
		// Arrange
		int year = 1000;
		long sequential = 1;

		// Act
		OrderNumber orderNumber = new OrderNumber(year, sequential);

		// Assert
		assertNotNull(orderNumber);
		assertEquals("ORD-1000-00001", orderNumber.getFormatted());
	}

	@Test @DisplayName("Deve aceitar ano máximo válido")
	void shouldAcceptMaximumValidYear() {
		// Arrange
		int year = 9999;
		long sequential = 1;

		// Act
		OrderNumber orderNumber = new OrderNumber(year, sequential);

		// Assert
		assertNotNull(orderNumber);
		assertEquals("ORD-9999-00001", orderNumber.getFormatted());
	}

	@Test @DisplayName("Deve aceitar sequencial zero")
	void shouldAcceptZeroSequential() {
		// Arrange
		int year = 2024;
		long sequential = 0;

		// Act
		OrderNumber orderNumber = new OrderNumber(year, sequential);

		// Assert
		assertNotNull(orderNumber);
		assertEquals("ORD-2024-00000", orderNumber.getFormatted());
	}
}
