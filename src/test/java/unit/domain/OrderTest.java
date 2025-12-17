package unit.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.order.core.domain.exceptions.OrderException;
import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.core.domain.model.OrderItem;
import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;

import unit.fixtures.OrderFixture;

@DisplayName("Order - Testes de Domínio")
class OrderTest {

	@Test @DisplayName("Deve criar pedido válido com itens")
	void shouldCreateValidOrderWithItems() {
		// Arrange & Act
		Order order = OrderFixture.createValidOrder();

		// Assert
		assertNotNull(order);
		assertEquals("A23basb3u123", order.getUserId());
		assertEquals(OrderStatus.RECEIVED, order.getOrderStatus());
		assertEquals(1, order.getOrderItems().size());
		assertNotNull(order.getOrderNumber());
		assertTrue(order.getOrderNumber().startsWith("ORD-"));
	}

	@Test @DisplayName("Deve calcular valor total corretamente")
	void shouldCalculateTotalAmountCorrectly() {
		// Arrange
		Order order = OrderFixture.createValidOrder();

		// Act
		BigDecimal totalAmount = order.getAmount();

		// Assert
		// (2 * 25.90) = 51.80
		assertEquals(new BigDecimal("51.80"), totalAmount);
	}

	@Test @DisplayName("Deve adicionar item ao pedido")
	void shouldAddItemToOrder() {
		// Arrange
		Order order = OrderFixture.createOrderWithSingleItem();
		OrderItem newItem = OrderFixture.createExpensiveOrderItem();

		// Act
		order.addItem(newItem);

		// Assert
		assertEquals(2, order.getOrderItems().size());
		assertTrue(order.getOrderItems().contains(newItem));
		assertEquals(newItem.getOrder(), order);
	}

	@Test @DisplayName("Deve remover item do pedido")
	void shouldRemoveItemFromOrder() {
		// Arrange
		Order order = OrderFixture.createOrderWithMultipleItems();
		OrderItem itemToRemove = order.getOrderItems().get(0);
		int initialSize = order.getOrderItems().size();

		// Act
		order.removeItem(itemToRemove);

		// Assert
		assertEquals(initialSize - 1, order.getOrderItems().size());
		assertFalse(order.getOrderItems().contains(itemToRemove));
	}

	@Test @DisplayName("Deve recalcular total após adicionar item")
	void shouldRecalculateTotalAfterAddingItem() {
		// Arrange
		Order order = OrderFixture.createOrderWithSingleItem();
		BigDecimal initialAmount = order.getAmount();
		OrderItem newItem = OrderFixture.createOrderItemWithObservations();

		// Act
		order.addItem(newItem);

		// Assert
		assertTrue(order.getAmount().compareTo(initialAmount) > 0);
		assertEquals(new BigDecimal("66.80"), order.getAmount());
	}

	@Test @DisplayName("Deve recalcular total após remover item")
	void shouldRecalculateTotalAfterRemovingItem() {
		// Arrange
		Order order = OrderFixture.createOrderWithMultipleItems();
		BigDecimal initialAmount = order.getAmount();
		OrderItem itemToRemove = order.getOrderItems().get(0);

		// Act
		order.removeItem(itemToRemove);

		// Assert
		assertTrue(order.getAmount().compareTo(initialAmount) < 0);
	}

	@Test @DisplayName("Deve lançar exceção para lista de itens nula")
	void shouldThrowExceptionForNullItemsList() {
		// Arrange & Act & Assert
		NullPointerException exception = assertThrows(NullPointerException.class, () -> {
			new Order("as23as3", null);
		});

		assertEquals("A lista de itens do pedido não pode ser nula", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção para lista de itens vazia")
	void shouldThrowExceptionForEmptyItemsList() {
		// Arrange
		List<OrderItem> emptyItems = Collections.emptyList();

		// Act & Assert
		OrderException exception = assertThrows(OrderException.class, () -> {
			new Order("as23as3", emptyItems);
		});

		assertEquals("O pedido deve conter itens", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção ao adicionar item nulo")
	void shouldThrowExceptionWhenAddingNullItem() {
		// Arrange
		Order order = OrderFixture.createValidOrder();

		// Act & Assert
		NullPointerException exception = assertThrows(NullPointerException.class, () -> {
			order.addItem(null);
		});

		assertEquals("O item do pedido não pode ser nulo", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção ao remover item nulo")
	void shouldThrowExceptionWhenRemovingNullItem() {
		// Arrange
		Order order = OrderFixture.createValidOrder();

		// Act & Assert
		NullPointerException exception = assertThrows(NullPointerException.class, () -> {
			order.removeItem(null);
		});

		assertEquals("O item do pedido não pode ser nulo", exception.getMessage());
	}

	@Test @DisplayName("Deve atualizar status do pedido")
	void shouldUpdateOrderStatus() {
		// Arrange
		Order order = OrderFixture.createValidOrder();

		// Act
		order.setOrderStatus(OrderStatus.PREPARING);

		// Assert
		assertEquals(OrderStatus.PREPARING, order.getOrderStatus());
	}

	@Test @DisplayName("Deve permitir transição válida de status")
	void shouldAllowValidStatusTransition() {
		// Arrange
		Order order = OrderFixture.createValidOrder();

		// Act & Assert
		assertDoesNotThrow(() -> {
			order.setOrderStatus(OrderStatus.PREPARING);
			order.setOrderStatus(OrderStatus.READY);
			order.setOrderStatus(OrderStatus.COMPLETED);
		});
	}

	@Test @DisplayName("Deve lançar exceção para status nulo")
	void shouldThrowExceptionForNullStatus() {
		// Arrange
		Order order = OrderFixture.createValidOrder();

		// Act & Assert
		NullPointerException exception = assertThrows(NullPointerException.class, () -> {
			order.setOrderStatus(null);
		});

		assertEquals("O status do pedido não pode ser nulo", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção ao alterar status de pedido cancelado")
	void shouldThrowExceptionWhenChangingCancelledOrderStatus() {
		// Arrange
		Order order = OrderFixture.createValidOrder();
		order.setOrderStatus(OrderStatus.CANCELLED);

		// Act & Assert
		OrderException exception = assertThrows(OrderException.class, () -> {
			order.setOrderStatus(OrderStatus.PREPARING);
		});

		assertEquals("Não é possível alterar o status de um pedido cancelado", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção ao alterar status de pedido completado")
	void shouldThrowExceptionWhenChangingCompletedOrderStatus() {
		// Arrange
		Order order = OrderFixture.createValidOrder();
		order.setOrderStatus(OrderStatus.PREPARING);
		order.setOrderStatus(OrderStatus.READY);
		order.setOrderStatus(OrderStatus.COMPLETED);

		// Act & Assert
		OrderException exception = assertThrows(OrderException.class, () -> {
			order.setOrderStatus(OrderStatus.PREPARING);
		});

		assertEquals("Não é possível alterar o status de um pedido já entregue ao cliente", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção ao cancelar pedido em preparação")
	void shouldThrowExceptionWhenCancellingOrderInPreparation() {
		// Arrange
		Order order = OrderFixture.createValidOrder();
		order.setOrderStatus(OrderStatus.PREPARING);

		// Act & Assert
		OrderException exception = assertThrows(OrderException.class, () -> {
			order.setOrderStatus(OrderStatus.CANCELLED);
		});

		assertEquals("Não é possível alterar o status de um pedido para cancelado após o início do seu preparo",
				exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção ao retornar para status RECEIVED")
	void shouldThrowExceptionWhenReturningToReceivedStatus() {
		// Arrange
		Order order = OrderFixture.createValidOrder();
		order.setOrderStatus(OrderStatus.PREPARING);

		// Act & Assert
		OrderException exception = assertThrows(OrderException.class, () -> {
			order.setOrderStatus(OrderStatus.RECEIVED);
		});

		assertTrue(exception.getMessage().contains("Não é possível retornar o status da ordem para"));
	}

	@Test @DisplayName("Deve aplicar desconto válido")
	void shouldApplyValidDiscount() {
		// Arrange
		Order order = OrderFixture.createValidOrder();
		BigDecimal originalAmount = order.getAmount();
		int discountPercent = 10;

		// Act
		order.applyDiscount(discountPercent);

		// Assert
		assertTrue(order.getAmount().compareTo(originalAmount) < 0);
		// Verifica apenas se o desconto foi aplicado (valor menor que o original)
		assertNotEquals(originalAmount, order.getAmount());
	}

	@Test @DisplayName("Deve lançar exceção para desconto inválido - muito baixo")
	void shouldThrowExceptionForInvalidDiscountTooLow() {
		// Arrange
		Order order = OrderFixture.createValidOrder();

		// Act & Assert
		OrderException exception = assertThrows(OrderException.class, () -> {
			order.applyDiscount(0);
		});

		assertEquals("O percentual de desconto deve estar entre 1 e 95", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção para desconto inválido - muito alto")
	void shouldThrowExceptionForInvalidDiscountTooHigh() {
		// Arrange
		Order order = OrderFixture.createValidOrder();

		// Act & Assert
		OrderException exception = assertThrows(OrderException.class, () -> {
			order.applyDiscount(96);
		});

		assertEquals("O percentual de desconto deve estar entre 1 e 95", exception.getMessage());
	}

	@Test @DisplayName("Deve retornar lista imutável de itens")
	void shouldReturnImmutableItemsList() {
		// Arrange
		Order order = OrderFixture.createValidOrder();

		// Act
		List<OrderItem> items = order.getOrderItems();

		// Assert
		assertThrows(UnsupportedOperationException.class, () -> {
			items.add(OrderFixture.createExpensiveOrderItem());
		});
	}

	@Test @DisplayName("Deve retornar datas de auditoria")
	void shouldReturnAuditDates() {
		// Arrange
		Order order = OrderFixture.createValidOrder();

		// Act & Assert
		assertNotNull(order.getCreatedAt());
		assertNotNull(order.getUpdatedAt());
	}

	@Test @DisplayName("Deve atualizar data de modificação ao alterar status")
	void shouldUpdateModificationDateWhenChangingStatus() {
		// Arrange
		Order order = OrderFixture.createValidOrder();
		order.markUpdatedNow();

		// Act
		order.setOrderStatus(OrderStatus.PREPARING);

		// Assert
		assertNotNull(order.getUpdatedAt());
	}

	@Test @DisplayName("Não deve criar ordem sem usuário")
	void shouldNotCreateOrderWithoutUser() {
		// Act & Assert
		var exception = assertThrows(OrderException.class, OrderFixture::createOrderWithoutUser);

		assertEquals("O id do usuário não pode estar vazio", exception.getMessage());
	}
}
