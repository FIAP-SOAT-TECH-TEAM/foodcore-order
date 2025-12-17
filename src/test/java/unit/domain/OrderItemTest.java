package unit.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.order.core.domain.exceptions.OrderItemException;
import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.core.domain.model.OrderItem;
import com.soat.fiap.food.core.order.core.domain.vo.OrderItemPrice;

import unit.fixtures.OrderFixture;

@DisplayName("OrderItem - Testes de Domínio")
class OrderItemTest {

	@Test @DisplayName("Deve criar item de pedido válido")
	void shouldCreateValidOrderItem() {
		// Arrange & Act
		OrderItem item = OrderFixture.createValidOrderItem();

		// Assert
		assertNotNull(item);
		assertEquals(1L, item.getProductId());
		assertEquals("Big Mac", item.getName());
		assertNull(item.getObservations());
		assertEquals(new BigDecimal("51.80"), item.getSubTotal());
	}

	@Test @DisplayName("Deve lançar exceção para productId nulo")
	void shouldThrowExceptionForNullProductId() {
		// Arrange
		var price = new OrderItemPrice(1, new BigDecimal("20.00"));

		// Act & Assert
		NullPointerException exception = assertThrows(NullPointerException.class, () -> {
			new OrderItem(null, "Item", price, "");
		});

		// Assert
		assertEquals("O ID do produto não pode ser nulo", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção para orderItemPrice nulo")
	void shouldThrowExceptionForNullPrice() {
		// Arrange & Act
		NullPointerException exception = assertThrows(NullPointerException.class, () -> {
			new OrderItem(1L, "Item", null, "");
		});

		// Assert
		assertEquals("O preço do item do pedido não pode ser nulo", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção para nome vazio")
	void shouldThrowExceptionForEmptyName() {
		// Arrange
		var price = new OrderItemPrice(1, new BigDecimal("10.00"));

		// Act
		OrderItemException exception = assertThrows(OrderItemException.class, () -> {
			new OrderItem(1L, "", price, "");
		});

		// Assert
		assertEquals("O nome do item do pedido não pode ser nulo", exception.getMessage());
	}

	@Test @DisplayName("Deve retornar subtotal corretamente")
	void shouldReturnCorrectSubtotal() {
		// Arrange
		OrderItem item = OrderFixture.createOrderItem("Produto", 10L, new BigDecimal("15.50"), 3);

		// Act & Assert
		assertEquals(new BigDecimal("46.50"), item.getSubTotal());
	}

	@Test @DisplayName("Deve retornar quantidade corretamente")
	void shouldReturnCorrectQuantity() {
		// Arrange
		OrderItem item = OrderFixture.createOrderItem("Produto", 2L, new BigDecimal("9.90"), 5);

		// Act & Assert
		assertEquals(5, item.getQuantity());
	}

	@Test @DisplayName("Deve retornar preço unitário corretamente")
	void shouldReturnCorrectUnitPrice() {
		// Arrange
		OrderItem item = OrderFixture.createOrderItem("Produto", 3L, new BigDecimal("8.30"), 2);

		// Act & Assert
		assertEquals(new BigDecimal("8.30"), item.getUnitPrice());
	}

	@Test @DisplayName("Deve retornar datas de auditoria")
	void shouldReturnAuditDates() {
		// Arrange & Act
		OrderItem item = OrderFixture.createValidOrderItem();

		// Assert
		assertNotNull(item.getCreatedAt());
		assertNotNull(item.getUpdatedAt());
	}

	@Test @DisplayName("Deve retornar ID da ordem quando associado a uma Order")
	void shouldReturnOrderIdWhenOrderIsAssigned() {
		// Arrange
		Order order = OrderFixture.createValidOrder();
		order.setId(99L);
		OrderItem item = OrderFixture.createValidOrderItem();

		// Act
		item.setOrder(order);

		// Assert
		assertEquals(99L, item.getOrderId());
	}

	@Test @DisplayName("Deve lançar NullPointerException se chamar getOrderId sem associar uma Order")
	void shouldThrowExceptionWhenCallingGetOrderIdWithoutOrder() {
		// Arrange
		OrderItem item = OrderFixture.createValidOrderItem();

		// Act
		NullPointerException exception = assertThrows(NullPointerException.class, item::getOrderId);

		// Assert
		assertEquals(
				"Cannot invoke \"com.soat.fiap.food.core.order.core.domain.model.Order.getId()\" because \"this.order\" is null",
				exception.getMessage());
	}
}
