package unit.controller;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.application.usecases.ApplyDiscountUseCase;
import com.soat.fiap.food.core.order.core.application.usecases.CreateOrderUseCase;
import com.soat.fiap.food.core.order.core.application.usecases.EnsureValidOrderItemsUseCase;
import com.soat.fiap.food.core.order.core.application.usecases.PublishOrderCreatedEventUseCase;
import com.soat.fiap.food.core.order.core.interfaceadapters.bff.controller.web.api.SaveOrderController;
import com.soat.fiap.food.core.order.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.EventPublisherSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.shared.infrastructure.common.source.AuthenticatedUserSource;

import unit.fixtures.OrderFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("SaveOrderController - Testes Unitários")
class SaveOrderControllerTest {

	@Mock
	private OrderDataSource orderDataSource;

	@Mock
	private CatalogDataSource catalogDataSource;

	@Mock
	private AuthenticatedUserSource authenticatedUserSource;

	@Mock
	private EventPublisherSource eventPublisherSource;

	@Test @DisplayName("Deve criar pedido com sucesso e publicar evento de criação")
	void shouldCreateOrderAndPublishEventSuccessfully() {
		// Arrange
		var request = OrderFixture.createValidCreateOrderRequest();
		var order = OrderFixture.createValidOrder();
		order.setId(1L);

		try (var createMock = mockStatic(CreateOrderUseCase.class);
				var validateMock = mockStatic(EnsureValidOrderItemsUseCase.class);
				var discountMock = mockStatic(ApplyDiscountUseCase.class);
				var publishMock = mockStatic(PublishOrderCreatedEventUseCase.class)) {

			createMock.when(() -> CreateOrderUseCase.createOrder(any())).thenReturn(order);
			when(orderDataSource.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

			// Act & Assert
			assertThatNoException().isThrownBy(() -> SaveOrderController.saveOrder(request, orderDataSource,
					catalogDataSource, authenticatedUserSource, eventPublisherSource));

			// Assert
			createMock.verify(() -> CreateOrderUseCase.createOrder(any()));
			validateMock.verify(() -> EnsureValidOrderItemsUseCase.ensureValidOrderItems(any(), any()));
			discountMock.verify(() -> ApplyDiscountUseCase.applyDiscount(any(), any()));
			publishMock.verify(() -> PublishOrderCreatedEventUseCase.publishCreateOrderEvent(any(), any()));
		}
	}

	@Test @DisplayName("Não deve publicar evento quando ocorrer falha na validação de itens")
	void shouldNotPublishEventWhenEnsureValidOrderItemsFails() {
		// Arrange
		var request = OrderFixture.createValidCreateOrderRequest();
		var order = OrderFixture.createValidOrder();

		try (var createMock = mockStatic(CreateOrderUseCase.class);
				var validateMock = mockStatic(EnsureValidOrderItemsUseCase.class);
				var publishMock = mockStatic(PublishOrderCreatedEventUseCase.class)) {

			createMock.when(() -> CreateOrderUseCase.createOrder(any())).thenReturn(order);
			validateMock.when(() -> EnsureValidOrderItemsUseCase.ensureValidOrderItems(any(), any()))
					.thenThrow(new RuntimeException());

			// Act
			try {
				SaveOrderController.saveOrder(request, orderDataSource, catalogDataSource, authenticatedUserSource,
						eventPublisherSource);
			} catch (Exception ignored) {
			}

			// Assert
			createMock.verify(() -> CreateOrderUseCase.createOrder(any()));
			publishMock.verifyNoInteractions();
		}
	}

	@Test @DisplayName("Não deve publicar evento quando aplicar desconto lançar exceção")
	void shouldNotPublishEventWhenApplyDiscountFails() {
		// Arrange
		var request = OrderFixture.createValidCreateOrderRequest();
		var order = OrderFixture.createValidOrder();

		try (var createMock = mockStatic(CreateOrderUseCase.class);
				var validateMock = mockStatic(EnsureValidOrderItemsUseCase.class);
				var discountMock = mockStatic(ApplyDiscountUseCase.class);
				var publishMock = mockStatic(PublishOrderCreatedEventUseCase.class)) {

			createMock.when(() -> CreateOrderUseCase.createOrder(any())).thenReturn(order);
			validateMock.when(() -> EnsureValidOrderItemsUseCase.ensureValidOrderItems(any(), any()))
					.thenAnswer(invocation -> null);
			discountMock.when(() -> ApplyDiscountUseCase.applyDiscount(any(), any())).thenThrow(new RuntimeException());

			// Act
			try {
				SaveOrderController.saveOrder(request, orderDataSource, catalogDataSource, authenticatedUserSource,
						eventPublisherSource);
			} catch (Exception ignored) {
			}

			// Assert
			publishMock.verifyNoInteractions();
		}
	}
}
