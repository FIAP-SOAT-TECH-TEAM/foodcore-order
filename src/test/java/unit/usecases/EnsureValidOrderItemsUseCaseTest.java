package unit.usecases;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.application.usecases.EnsureValidOrderItemsUseCase;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderItemException;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.CatalogGateway;

import unit.fixtures.CatalogFixture;
import unit.fixtures.OrderFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("EnsureValidOrderItemsUseCase - Testes Unitários")
class EnsureValidOrderItemsUseCaseTest {

	@Mock
	private CatalogGateway catalogGateway;

	@Test @DisplayName("Deve validar com sucesso quando o item for válido")
	void shouldValidateSuccessfullyWhenAllItemsAreValid() {
		// Arrange
		var catalog = CatalogFixture.createCatalogWithProducts();
		var orderItem = OrderFixture.createOrderItem(catalog.getFirst().name(), catalog.getFirst().id(),
				catalog.getFirst().price());

		when(catalogGateway.findByProductIds(List.of(catalog.getFirst().id()))).thenReturn(catalog);

		// Act & Assert
		assertThatNoException().isThrownBy(() -> EnsureValidOrderItemsUseCase
				.ensureValidOrderItems(Collections.singletonList(orderItem), catalogGateway));

		verify(catalogGateway).findByProductIds(List.of(catalog.getFirst().id()));
	}

	@Test @DisplayName("Deve lançar exceção quando produto não for encontrado")
	void shouldThrowExceptionWhenProductNotFound() {
		// Arrange
		var orderItem = OrderFixture.createBeverageOrderItem();

		when(catalogGateway.findByProductIds(List.of(2L))).thenReturn(Collections.emptyList());

		// Act & Assert
		assertThatThrownBy(() -> EnsureValidOrderItemsUseCase
				.ensureValidOrderItems(Collections.singletonList(orderItem), catalogGateway))
				.isInstanceOf(OrderItemException.class)
				.hasMessage("O produto do item do pedido não existe");

		verify(catalogGateway).findByProductIds(List.of(2L));
	}

	@Test @DisplayName("Deve validar múltiplos itens com sucesso")
	void shouldValidateMultipleItemsSuccessfully() {
		// Arrange
		var productsDto = CatalogFixture.createCatalogWithMultipleProducts();

		var orderItem1 = OrderFixture.createOrderItem(productsDto.getFirst().name(), productsDto.getFirst().id(),
				productsDto.getFirst().price());
		var orderItem2 = OrderFixture.createOrderItem(productsDto.getLast().name(), productsDto.getLast().id(),
				productsDto.getLast().price());
		var orderItems = List.of(orderItem1, orderItem2);

		when(catalogGateway.findByProductIds(List.of(1L, 2L))).thenReturn(productsDto);

		// Act & Assert
		assertThatNoException()
				.isThrownBy(() -> EnsureValidOrderItemsUseCase.ensureValidOrderItems(orderItems, catalogGateway));

		verify(catalogGateway).findByProductIds(List.of(1L, 2L));
	}

	@Test @DisplayName("Deve lançar exceção quando o nome do produto divergir")
	void shouldThrowExceptionWhenProductNameDiffers() {
		// Arrange
		var product = CatalogFixture.createCatalogProduct(1L, "Nome Original", new BigDecimal("10.0"), true, true, 10);
		var orderItem = OrderFixture.createOrderItem("Nome Errado", 1L, product.price());

		when(catalogGateway.findByProductIds(List.of(1L))).thenReturn(List.of(product));

		// Act & Assert
		assertThatThrownBy(() -> EnsureValidOrderItemsUseCase
				.ensureValidOrderItems(Collections.singletonList(orderItem), catalogGateway))
				.isInstanceOf(OrderItemException.class)
				.hasMessage("O nome do produto do item diverge do nome do produto cadastrado");

		verify(catalogGateway).findByProductIds(List.of(1L));
	}

	@Test @DisplayName("Deve lançar exceção quando o preço unitário divergir")
	void shouldThrowExceptionWhenProductPriceDiffers() {
		// Arrange
		var product = CatalogFixture.createCatalogProduct(1L, "Coca", new BigDecimal("10.0"), true, true, 10);
		var orderItem = OrderFixture.createOrderItem("Coca", 1L, new BigDecimal("11.0"));

		when(catalogGateway.findByProductIds(List.of(1L))).thenReturn(List.of(product));

		// Act & Assert
		assertThatThrownBy(() -> EnsureValidOrderItemsUseCase
				.ensureValidOrderItems(Collections.singletonList(orderItem), catalogGateway))
				.isInstanceOf(OrderItemException.class)
				.hasMessage("O preço unitário do item do pedido diverge do preço do produto");

		verify(catalogGateway).findByProductIds(List.of(1L));
	}

	@Test @DisplayName("Deve lançar exceção quando o produto estiver inativo")
	void shouldThrowExceptionWhenProductIsInactive() {
		// Arrange
		var product = CatalogFixture.createCatalogProduct(1L, "Coca", new BigDecimal("10.0"), false, true, 10);
		var orderItem = OrderFixture.createOrderItem("Coca", 1L, new BigDecimal("10.0"));

		when(catalogGateway.findByProductIds(List.of(1L))).thenReturn(List.of(product));

		// Act & Assert
		assertThatThrownBy(() -> EnsureValidOrderItemsUseCase
				.ensureValidOrderItems(Collections.singletonList(orderItem), catalogGateway))
				.isInstanceOf(OrderItemException.class)
				.hasMessage("O pedido não pode possuir produtos inativos");

		verify(catalogGateway).findByProductIds(List.of(1L));
	}

	@Test @DisplayName("Deve lançar exceção quando a categoria do produto estiver inativa")
	void shouldThrowExceptionWhenProductCategoryIsInactive() {
		// Arrange
		var product = CatalogFixture.createCatalogProduct(1L, "Coca", new BigDecimal("10.0"), true, false, 10);
		var orderItem = OrderFixture.createOrderItem("Coca", 1L, new BigDecimal("10.0"));

		when(catalogGateway.findByProductIds(List.of(1L))).thenReturn(List.of(product));

		// Act & Assert
		assertThatThrownBy(() -> EnsureValidOrderItemsUseCase
				.ensureValidOrderItems(Collections.singletonList(orderItem), catalogGateway))
				.isInstanceOf(OrderItemException.class)
				.hasMessage("A categoria do produto do pedido não pode estar inativa");

		verify(catalogGateway).findByProductIds(List.of(1L));
	}

	@Test @DisplayName("Deve lançar exceção quando o estoque for insuficiente")
	void shouldThrowExceptionWhenStockIsInsufficient() {
		// Arrange
		var product = CatalogFixture.createCatalogProduct(1L, "Coca", new BigDecimal("10.0"), true, true, 1);
		var orderItem = OrderFixture.createOrderItem("Coca", 1L, new BigDecimal("10.0"), 5);

		when(catalogGateway.findByProductIds(List.of(1L))).thenReturn(List.of(product));

		// Act & Assert
		assertThatThrownBy(() -> EnsureValidOrderItemsUseCase
				.ensureValidOrderItems(Collections.singletonList(orderItem), catalogGateway))
				.isInstanceOf(OrderItemException.class)
				.hasMessage("Quantidade insuficiente em estoque para o produto: Coca");

		verify(catalogGateway).findByProductIds(List.of(1L));
	}

}
