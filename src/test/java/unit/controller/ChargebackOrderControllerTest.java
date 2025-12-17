package unit.controller;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.application.usecases.EnsureOrderPaymentIsValidUseCase;
import com.soat.fiap.food.core.order.core.application.usecases.UpdateOrderStatusUseCase;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderAlreadyHasStatusException;
import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.bff.controller.web.api.ChargebackOrderController;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.PaymentDataSource;

import unit.fixtures.OrderFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("ChargebackOrderController - Testes Unitários")
class ChargebackOrderControllerTest {

	@Mock
	private OrderDataSource orderDataSource;

	@Mock
	private PaymentDataSource paymentDataSource;

	@Test @DisplayName("Deve estornar pedido com sucesso")
	void shouldChargebackOrderSuccessfully() {
		// Arrange
		Long id = 10L;

		var order = OrderFixture.createValidOrder();
		order.setId(id);
		order.setOrderStatus(OrderStatus.RECEIVED);

		try (var ensureMock = mockStatic(EnsureOrderPaymentIsValidUseCase.class);
				var updateMock = mockStatic(UpdateOrderStatusUseCase.class)) {

			updateMock.when(() -> UpdateOrderStatusUseCase.updateOrderStatus(eq(id), eq(OrderStatus.CANCELLED), any()))
					.thenAnswer(inv -> {
						order.setOrderStatus(OrderStatus.CANCELLED);
						return order;
					});
			when(orderDataSource.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

			// Act & Assert
			assertThatNoException().isThrownBy(
					() -> ChargebackOrderController.chargebackOrder(id, orderDataSource, paymentDataSource));

			ensureMock.verify(() -> EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(eq(id),
					eq(OrderStatus.CANCELLED), any(), any()));

			updateMock
					.verify(() -> UpdateOrderStatusUseCase.updateOrderStatus(eq(id), eq(OrderStatus.CANCELLED), any()));
		}
	}

	@Test @DisplayName("Não deve atualizar status quando EnsureOrderPaymentIsValid lançar exceção")
	void shouldNotUpdateStatusWhenEnsurePaymentValidationFails() {
		// Arrange
		Long id = 20L;

		try (var ensureMock = mockStatic(EnsureOrderPaymentIsValidUseCase.class);
				var updateMock = mockStatic(UpdateOrderStatusUseCase.class)) {

			ensureMock
					.when(() -> EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(any(), any(), any(), any()))
					.thenThrow(new RuntimeException("Falha na validação"));

			// Act
			try {
				ChargebackOrderController.chargebackOrder(id, orderDataSource, paymentDataSource);
			} catch (Exception ignore) {
			}

			// Assert
			updateMock.verifyNoInteractions();
		}
	}

	@Test @DisplayName("Não deve atualizar status quando pedido já estiver estornado")
	void shouldNotUpdateStatusWhenOrderAlreadyCancelled() {
		// Arrange
		Long id = 30L;

		try (var ensureMock = mockStatic(EnsureOrderPaymentIsValidUseCase.class);
				var updateMock = mockStatic(UpdateOrderStatusUseCase.class)) {

			updateMock.when(() -> UpdateOrderStatusUseCase.updateOrderStatus(eq(id), eq(OrderStatus.CANCELLED), any()))
					.thenThrow(new OrderAlreadyHasStatusException("Pedido já cancelado"));

			// Act
			assertThatNoException().isThrownBy(
					() -> ChargebackOrderController.chargebackOrder(id, orderDataSource, paymentDataSource));

			// Assert
			ensureMock.verify(() -> EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(eq(id),
					eq(OrderStatus.CANCELLED), any(), any()));
		}
	}
}
