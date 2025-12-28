package unit.controller.order;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.application.usecases.*;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderAlreadyHasStatusException;
import com.soat.fiap.food.core.order.core.domain.vo.OrderStatus;
import com.soat.fiap.food.core.order.core.interfaceadapters.controller.UpdateOrderStatusController;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.mappers.OrderDTOMapper;
import com.soat.fiap.food.core.order.infrastructure.common.source.EventPublisherSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.PaymentDataSource;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.OrderStatusRequest;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.response.OrderStatusResponse;

import unit.fixtures.OrderFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("UpdateOrderStatusController - Testes Unitários")
class UpdateOrderStatusControllerTest {

	@Mock
	private OrderDataSource orderDataSource;

	@Mock
	private PaymentDataSource paymentDataSource;

	@Mock
	private EventPublisherSource eventPublisherSource;

	@Test @DisplayName("Deve atualizar status do pedido com sucesso")
	void shouldUpdateOrderStatusSuccessfully() {

		// Arrange
		var orderId = 1L;
		var request = new OrderStatusRequest(OrderStatus.PREPARING);
		var order = OrderFixture.createValidOrder();
		order.setId(orderId);

		var orderDTO = OrderDTOMapper.toDTO(order);

		try (MockedStatic<EnsureOrderPaymentIsValidUseCase> paymentValidationMock = mockStatic(
				EnsureOrderPaymentIsValidUseCase.class);
				MockedStatic<UpdateOrderStatusUseCase> updateStatusMock = mockStatic(UpdateOrderStatusUseCase.class)) {

			paymentValidationMock.when(() -> EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(eq(orderId),
					eq(OrderStatus.PREPARING), any(), any())).thenAnswer(invocation -> null);

			updateStatusMock.when(
					() -> UpdateOrderStatusUseCase.updateOrderStatus(eq(orderId), eq(OrderStatus.PREPARING), any()))
					.thenReturn(order);

			when(orderDataSource.save(any())).thenReturn(orderDTO);

			// Act
			OrderStatusResponse response = UpdateOrderStatusController.updateOrderStatus(orderId, request,
					orderDataSource, paymentDataSource, eventPublisherSource);

			// Assert
			assertNotNull(response);
			verify(orderDataSource, times(1)).save(any());
		}
	}

	@Test @DisplayName("Deve publicar evento de pedido cancelado quando status for CANCELLED")
	void shouldPublishCanceledEventWhenStatusIsCancelled() {

		// Arrange
		var orderId = 2L;
		var request = new OrderStatusRequest(OrderStatus.CANCELLED);
		var order = OrderFixture.createValidOrder();
		order.setId(orderId);
		order.setOrderStatus(OrderStatus.CANCELLED);

		var orderDTO = OrderDTOMapper.toDTO(order);

		try (MockedStatic<EnsureOrderPaymentIsValidUseCase> paymentValidationMock = mockStatic(
				EnsureOrderPaymentIsValidUseCase.class);
				MockedStatic<UpdateOrderStatusUseCase> updateStatusMock = mockStatic(UpdateOrderStatusUseCase.class);
				MockedStatic<PublishOrderCanceledEventUseCase> publishCanceledMock = mockStatic(
						PublishOrderCanceledEventUseCase.class)) {

			paymentValidationMock
					.when(() -> EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(any(), any(), any(), any()))
					.thenAnswer(invocation -> null);

			updateStatusMock.when(() -> UpdateOrderStatusUseCase.updateOrderStatus(any(), any(), any()))
					.thenReturn(order);

			when(orderDataSource.save(any())).thenReturn(orderDTO);

			// Act
			UpdateOrderStatusController.updateOrderStatus(orderId, request, orderDataSource, paymentDataSource,
					eventPublisherSource);

			// Assert
			publishCanceledMock.verify(() -> PublishOrderCanceledEventUseCase.publishOrderCanceledEvent(any(), any()),
					times(1));
		}
	}

	@Test @DisplayName("Deve publicar evento de pedido pronto quando status for READY")
	void shouldPublishReadyEventWhenStatusIsReady() {

		// Arrange
		var orderId = 3L;
		var request = new OrderStatusRequest(OrderStatus.READY);
		var order = OrderFixture.createValidOrder();
		order.setOrderStatus(OrderStatus.READY);
		order.setId(orderId);

		var orderDTO = OrderDTOMapper.toDTO(order);

		try (MockedStatic<EnsureOrderPaymentIsValidUseCase> paymentValidationMock = mockStatic(
				EnsureOrderPaymentIsValidUseCase.class);
				MockedStatic<UpdateOrderStatusUseCase> updateStatusMock = mockStatic(UpdateOrderStatusUseCase.class);
				MockedStatic<PublishOrderReadyEventUseCase> publishReadyMock = mockStatic(
						PublishOrderReadyEventUseCase.class)) {

			paymentValidationMock
					.when(() -> EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(any(), any(), any(), any()))
					.thenAnswer(invocation -> null);

			updateStatusMock.when(() -> UpdateOrderStatusUseCase.updateOrderStatus(any(), any(), any()))
					.thenReturn(order);

			when(orderDataSource.save(any())).thenReturn(orderDTO);

			// Act
			UpdateOrderStatusController.updateOrderStatus(orderId, request, orderDataSource, paymentDataSource,
					eventPublisherSource);

			// Assert
			publishReadyMock.verify(() -> PublishOrderReadyEventUseCase.publishCreateOrderEvent(any(), any()),
					times(1));
		}
	}

	@Test @DisplayName("Deve retornar pedido existente quando status já for o mesmo")
	void shouldReturnExistingOrderWhenStatusAlreadyExists() {

		// Arrange
		var orderId = 4L;
		var request = new OrderStatusRequest(OrderStatus.PREPARING);
		var existingOrder = OrderFixture.createValidOrder();

		try (MockedStatic<EnsureOrderPaymentIsValidUseCase> paymentValidationMock = mockStatic(
				EnsureOrderPaymentIsValidUseCase.class);
				MockedStatic<GetOrderByIdUseCase> getOrderMock = mockStatic(GetOrderByIdUseCase.class);
				MockedStatic<UpdateOrderStatusUseCase> updateStatusMock = mockStatic(UpdateOrderStatusUseCase.class)) {

			paymentValidationMock
					.when(() -> EnsureOrderPaymentIsValidUseCase.ensureOrderPaymentIsValid(any(), any(), any(), any()))
					.thenThrow(new OrderAlreadyHasStatusException("Status já definido"));

			getOrderMock.when(() -> GetOrderByIdUseCase.getOrderById(eq(orderId), any())).thenReturn(existingOrder);

			// Act & Assert
			assertDoesNotThrow(() -> UpdateOrderStatusController.updateOrderStatus(orderId, request, orderDataSource,
					paymentDataSource, eventPublisherSource));
		}
	}
}
