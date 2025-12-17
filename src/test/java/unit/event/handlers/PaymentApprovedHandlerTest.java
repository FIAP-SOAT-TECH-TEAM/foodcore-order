package unit.event.handlers;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.bff.controller.web.api.UpdateOrderStatusController;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.PaymentApprovedEventDto;
import com.soat.fiap.food.core.order.infrastructure.common.source.EventPublisherSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.PaymentDataSource;
import com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.payment.handlers.PaymentApprovedHandler;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.OrderStatusRequest;

import unit.fixtures.EventFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("PaymentApprovedHandler - Testes Unitários")
class PaymentApprovedHandlerTest {

	@Mock
	private OrderDataSource orderDataSource;

	@Mock
	private PaymentDataSource paymentDataSource;

	@Mock
	private EventPublisherSource eventPublisherSource;

	private PaymentApprovedHandler handler;

	@BeforeEach
	void setUp() {
		handler = new PaymentApprovedHandler(orderDataSource, paymentDataSource, eventPublisherSource);
	}

	@Test @DisplayName("Deve processar evento de pagamento aprovado com sucesso")
	void shouldHandlePaymentApprovedEventSuccessfully() {
		// Arrange
		PaymentApprovedEventDto event = EventFixture.createPaymentApprovedEventDto(UUID.randomUUID(), 1L,
				BigDecimal.valueOf(100.00), "CREDIT_CARD", LocalDateTime.now());

		try (MockedStatic<UpdateOrderStatusController> mockedStatic = mockStatic(UpdateOrderStatusController.class)) {
			// Act
			assertThatNoException().isThrownBy(() -> handler.handle(event));

			// Assert
			mockedStatic.verify(() -> UpdateOrderStatusController.updateOrderStatus(eq(1L),
					eq(new OrderStatusRequest(OrderStatus.PREPARING)), eq(orderDataSource), eq(paymentDataSource),
					eq(eventPublisherSource)), times(1));
		}
	}

	@Test @DisplayName("Deve processar evento com valor personalizado")
	void shouldHandlePaymentApprovedEventWithCustomAmount() {
		// Arrange
		PaymentApprovedEventDto event = EventFixture.createPaymentApprovedEventDto(UUID.randomUUID(), 2L,
				BigDecimal.valueOf(999.99), "PIX", LocalDateTime.now());

		try (MockedStatic<UpdateOrderStatusController> mockedStatic = mockStatic(UpdateOrderStatusController.class)) {
			// Act
			assertThatNoException().isThrownBy(() -> handler.handle(event));

			// Assert
			mockedStatic.verify(() -> UpdateOrderStatusController.updateOrderStatus(eq(2L),
					eq(new OrderStatusRequest(OrderStatus.PREPARING)), eq(orderDataSource), eq(paymentDataSource),
					eq(eventPublisherSource)), times(1));
		}
	}

	@Test @DisplayName("Deve processar múltiplos eventos de pagamento aprovado")
	void shouldHandleMultiplePaymentApprovedEvents() {
		// Arrange
		PaymentApprovedEventDto event1 = EventFixture.createPaymentApprovedEventDto(UUID.randomUUID(), 3L,
				BigDecimal.valueOf(150.50), "DEBIT_CARD", LocalDateTime.now());

		PaymentApprovedEventDto event2 = EventFixture.createPaymentApprovedEventDto(UUID.randomUUID(), 4L,
				BigDecimal.valueOf(200.00), "CREDIT_CARD", LocalDateTime.now());

		try (MockedStatic<UpdateOrderStatusController> mockedStatic = mockStatic(UpdateOrderStatusController.class)) {
			// Act
			handler.handle(event1);
			handler.handle(event2);

			// Assert
			mockedStatic.verify(() -> UpdateOrderStatusController.updateOrderStatus(eq(3L),
					eq(new OrderStatusRequest(OrderStatus.PREPARING)), eq(orderDataSource), eq(paymentDataSource),
					eq(eventPublisherSource)), times(1));

			mockedStatic.verify(() -> UpdateOrderStatusController.updateOrderStatus(eq(4L),
					eq(new OrderStatusRequest(OrderStatus.PREPARING)), eq(orderDataSource), eq(paymentDataSource),
					eq(eventPublisherSource)), times(1));
		}
	}
}
