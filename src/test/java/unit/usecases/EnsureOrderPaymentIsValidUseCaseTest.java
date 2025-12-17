package unit.usecases;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.application.usecases.EnsureOrderPaymentIsValidUseCase;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderNotFoundException;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderPaymentException;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderPaymentNotFoundException;
import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.OrderGateway;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.PaymentGateway;

import unit.fixtures.OrderFixture;
import unit.fixtures.PaymentFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("EnsureOrderPaymentIsValidUseCase - Testes Unitários")
class EnsureOrderPaymentIsValidUseCaseTest {

	@Mock
	private PaymentGateway paymentGateway;

	@Mock
	private OrderGateway orderGateway;

	@Test @DisplayName("Deve validar com sucesso quando pedido e pagamento existem e estão aprovados")
	void shouldValidateSuccessfullyWhenOrderAndPaymentExistAndApproved() {
		// Arrange
		var orderId = 1L;
		var order = OrderFixture.createValidOrder();
		order.setOrderStatus(OrderStatus.PREPARING);
		var payment = PaymentFixture.createApprovedPaymentStatus(orderId);

		when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));
		when(paymentGateway.getOrderStatus(orderId)).thenReturn(payment);

		// Act & Assert
		assertThatNoException().isThrownBy(() -> EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(orderId,
				OrderStatus.PREPARING, paymentGateway, orderGateway));

		verify(orderGateway).findById(orderId);
		verify(paymentGateway).getOrderStatus(orderId);
	}

	@Test @DisplayName("Deve lançar exceção quando pedido não for encontrado")
	void shouldThrowExceptionWhenOrderNotFound() {
		// Arrange
		var orderId = 999L;
		when(orderGateway.findById(orderId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(orderId,
				OrderStatus.RECEIVED, paymentGateway, orderGateway)).isInstanceOf(OrderNotFoundException.class)
				.hasMessageContaining("Pedido não encontrado com id: 999");

		verify(orderGateway).findById(orderId);
	}

	@Test @DisplayName("Deve lançar exceção quando pagamento não existir e status não for RECEIVED")
	void shouldThrowExceptionWhenPaymentNotExistsAndStatusNotReceived() {
		// Arrange
		var orderId = 2L;
		var order = OrderFixture.createValidOrder();
		order.setOrderStatus(OrderStatus.PREPARING);

		when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));
		when(paymentGateway.getOrderStatus(orderId)).thenReturn(null);

		// Act & Assert
		assertThatThrownBy(() -> EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(orderId,
				OrderStatus.PREPARING, paymentGateway, orderGateway)).isInstanceOf(OrderPaymentNotFoundException.class)
				.hasMessage("O pagamento do pedido não existe");

		verify(orderGateway).findById(orderId);
		verify(paymentGateway).getOrderStatus(orderId);
	}

	@Test @DisplayName("Deve validar com sucesso quando pagamento não existir mas status for RECEIVED")
	void shouldValidateSuccessfullyWhenPaymentNotExistsButStatusIsReceived() {
		// Arrange
		var orderId = 3L;
		var order = OrderFixture.createValidOrder();
		order.setOrderStatus(OrderStatus.RECEIVED);

		when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));
		when(paymentGateway.getOrderStatus(orderId)).thenReturn(null);

		// Act & Assert
		assertThatNoException().isThrownBy(() -> EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(orderId,
				OrderStatus.RECEIVED, paymentGateway, orderGateway));

		verify(orderGateway).findById(orderId);
		verify(paymentGateway).getOrderStatus(orderId);
	}

	@Test @DisplayName("Deve lançar exceção quando pagamento existe mas não está aprovado e status for em progresso")
	void shouldThrowExceptionWhenPaymentExistsButNotApprovedAndStatusInProgress() {
		// Arrange
		var orderId = 4L;
		var order = OrderFixture.createValidOrder();
		order.setOrderStatus(OrderStatus.PREPARING);
		var payment = PaymentFixture.createPendingPaymentStatus(orderId);

		when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));
		when(paymentGateway.getOrderStatus(orderId)).thenReturn(payment);

		// Act & Assert
		assertThatThrownBy(() -> EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(orderId, OrderStatus.READY,
				paymentGateway, orderGateway)).isInstanceOf(OrderPaymentException.class)
				.hasMessage("Somente pedidos pagos podem transacionar para o status: READY");

		verify(orderGateway).findById(orderId);
		verify(paymentGateway).getOrderStatus(orderId);
	}
}
