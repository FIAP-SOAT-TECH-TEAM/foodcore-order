package unit.vo;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.order.core.domain.vo.OrderItemPrice;

@DisplayName("OrderItemPrice - Testes de Value Object")
class OrderItemPriceTest {

	@Test @DisplayName("Deve criar OrderItemPrice válido")
	void shouldCreateValidOrderItemPrice() {
		// Arrange
		int quantity = 2;
		BigDecimal unitPrice = new BigDecimal("25.90");

		// Act
		OrderItemPrice orderItemPrice = new OrderItemPrice(quantity, unitPrice);

		// Assert
		assertNotNull(orderItemPrice);
		assertEquals(quantity, orderItemPrice.quantity());
		assertEquals(unitPrice, orderItemPrice.unitPrice());
	}

	@Test @DisplayName("Deve calcular subtotal corretamente")
	void shouldCalculateSubtotalCorrectly() {
		// Arrange
		int quantity = 3;
		BigDecimal unitPrice = new BigDecimal("15.50");
		OrderItemPrice orderItemPrice = new OrderItemPrice(quantity, unitPrice);

		// Act
		BigDecimal subtotal = orderItemPrice.getSubTotal();

		// Assert
		assertEquals(new BigDecimal("46.50"), subtotal);
	}

	@Test @DisplayName("Deve calcular subtotal com quantidade 1")
	void shouldCalculateSubtotalWithQuantityOne() {
		// Arrange
		int quantity = 1;
		BigDecimal unitPrice = new BigDecimal("29.99");
		OrderItemPrice orderItemPrice = new OrderItemPrice(quantity, unitPrice);

		// Act
		BigDecimal subtotal = orderItemPrice.getSubTotal();

		// Assert
		assertEquals(new BigDecimal("29.99"), subtotal);
	}

	@Test @DisplayName("Deve lançar exceção para quantidade zero")
	void shouldThrowExceptionForZeroQuantity() {
		// Arrange
		int quantity = 0;
		BigDecimal unitPrice = new BigDecimal("25.90");

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			new OrderItemPrice(quantity, unitPrice);
		});

		assertEquals("A quantidade do item deve ser maior que 0", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção para quantidade negativa")
	void shouldThrowExceptionForNegativeQuantity() {
		// Arrange
		int quantity = -1;
		BigDecimal unitPrice = new BigDecimal("25.90");

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			new OrderItemPrice(quantity, unitPrice);
		});

		assertEquals("A quantidade do item deve ser maior que 0", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção para preço unitário zero")
	void shouldThrowExceptionForZeroUnitPrice() {
		// Arrange
		int quantity = 2;
		BigDecimal unitPrice = BigDecimal.ZERO;

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			new OrderItemPrice(quantity, unitPrice);
		});

		assertEquals("O preço unitário do item deve ser maior que 0", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção para preço unitário negativo")
	void shouldThrowExceptionForNegativeUnitPrice() {
		// Arrange
		int quantity = 2;
		BigDecimal unitPrice = new BigDecimal("-10.00");

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			new OrderItemPrice(quantity, unitPrice);
		});

		assertEquals("O preço unitário do item deve ser maior que 0", exception.getMessage());
	}

	@Test @DisplayName("Deve calcular subtotal com valores decimais")
	void shouldCalculateSubtotalWithDecimalValues() {
		// Arrange
		int quantity = 2;
		BigDecimal unitPrice = new BigDecimal("12.345");
		OrderItemPrice orderItemPrice = new OrderItemPrice(quantity, unitPrice);

		// Act
		BigDecimal subtotal = orderItemPrice.getSubTotal();

		// Assert
		assertEquals(new BigDecimal("24.690"), subtotal);
	}
}
