package unit.presenter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.presenter.OrderStatusPresenter;

import unit.fixtures.OrderFixture;

@DisplayName("OrderStatusPresenter - Testes Unitários")
class OrderStatusPresenterTest {

	@Test @DisplayName("Deve converter pedido para OrderStatusResponse")
	void shouldConvertOrderToOrderStatusResponse() {

		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(2L);

		// Act
		var response = OrderStatusPresenter.toOrderStatusResponse(order);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getOrderId()).isEqualTo(order.getId());
		assertThat(response.getOrderStatus()).isEqualTo(order.getOrderStatus());
	}

	@Test @DisplayName("Deve converter pedido com status alterado para response")
	void shouldConvertOrderWithUpdatedStatusToResponse() {

		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(2L);
		order.setOrderStatus(OrderStatus.PREPARING);

		// Act
		var response = OrderStatusPresenter.toOrderStatusResponse(order);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.PREPARING);
		assertThat(response.getCreatedAt()).isNotNull();
		assertThat(response.getUpdatedAt()).isNotNull();
	}

	@Test @DisplayName("Deve executar conversão sem lançar exceções")
	void shouldExecuteConversionWithoutExceptions() {

		// Arrange
		var order = OrderFixture.createValidOrder();
		order.setId(2L);

		// Act
		var response = OrderStatusPresenter.toOrderStatusResponse(order);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getOrderId()).isNotNull();
	}
}
