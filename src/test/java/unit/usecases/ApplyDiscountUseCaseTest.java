package unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.order.core.application.usecases.ApplyDiscountUseCase;
import com.soat.fiap.food.core.order.core.domain.exceptions.OrderException;
import com.soat.fiap.food.core.shared.core.interfaceadapters.gateways.AuthenticatedUserGateway;

import unit.fixtures.OrderFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("ApplyDiscountUseCase - Testes Unitários")
class ApplyDiscountUseCaseTest {

	@Mock
	private AuthenticatedUserGateway authenticatedUserGateway;

	@Test @DisplayName("Deve aplicar desconto de 10% para usuário com 5 anos de cadastro")
	void shouldApply10PercentDiscountForUserWith5YearsOfRegistration() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		var originalAmount = order.getAmount(); // 51.80

		when(authenticatedUserGateway.getCreationDate()).thenReturn(OffsetDateTime.now().minusYears(5));

		// Act
		ApplyDiscountUseCase.applyDiscount(order, authenticatedUserGateway);

		// Assert
		assertThat(order.getAmount()).isLessThan(originalAmount);
		// Com 10% de desconto: 51.80 - (51.80 * 0.10) = 46.62
		assertThat(order.getAmount()).isEqualByComparingTo(new BigDecimal("46.6200"));

		verify(authenticatedUserGateway).getCreationDate();
	}

	@Test @DisplayName("Deve aplicar desconto de 20% para usuário com 10 anos de cadastro")
	void shouldApply20PercentDiscountForUserWith10YearsOfRegistration() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		var originalAmount = order.getAmount(); // 51.80

		when(authenticatedUserGateway.getCreationDate()).thenReturn(OffsetDateTime.now().minusYears(10));

		// Act
		ApplyDiscountUseCase.applyDiscount(order, authenticatedUserGateway);

		// Assert
		assertThat(order.getAmount()).isLessThan(originalAmount);
		// Com 20% de desconto: 51.80 - (51.80 * 0.20) = 41.44
		assertThat(order.getAmount()).isEqualByComparingTo(new BigDecimal("41.4400"));

		verify(authenticatedUserGateway).getCreationDate();
	}

	@Test @DisplayName("Não deve aplicar desconto para usuário com menos de 1 ano de cadastro")
	void shouldNotApplyDiscountForUserWithLessThan1YearOfRegistration() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		var originalAmount = order.getAmount(); // 51.80

		// Usuário criado no mesmo ano = diferença de anos = 0
		var currentYear = LocalDateTime.now().getYear();
		var sameYearDate = OffsetDateTime.of(currentYear, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);

		when(authenticatedUserGateway.getCreationDate()).thenReturn(sameYearDate);

		// Act
		ApplyDiscountUseCase.applyDiscount(order, authenticatedUserGateway);

		// Assert
		assertThat(order.getAmount()).isEqualByComparingTo(originalAmount);

		verify(authenticatedUserGateway).getCreationDate();
	}

	@Test @DisplayName("Deve aplicar desconto máximo de 94% para usuário muito antigo")
	void shouldApplyMaximumDiscountForVeryOldUser() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		var originalAmount = order.getAmount(); // 51.80

		when(authenticatedUserGateway.getCreationDate()).thenReturn(OffsetDateTime.now().minusYears(47));

		// Act
		ApplyDiscountUseCase.applyDiscount(order, authenticatedUserGateway);

		// Assert
		assertThat(order.getAmount()).isLessThan(originalAmount);
		// Com 94% de desconto: 51.80 - (51.80 * 0.94) = 3.108
		assertThat(order.getAmount()).isEqualByComparingTo(new BigDecimal("3.1080"));

		verify(authenticatedUserGateway).getCreationDate();
	}

	@Test @DisplayName("Não deve aplicar desconto superior a 95% mesmo para usuário muito antigo")
	void shouldNotApplyDiscountOver95PercentEvenForVeryOldUser() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		var originalAmount = order.getAmount(); // 51.80

		when(authenticatedUserGateway.getCreationDate()).thenReturn(OffsetDateTime.now().minusYears(50));

		// Act
		ApplyDiscountUseCase.applyDiscount(order, authenticatedUserGateway);

		// Assert
		assertThat(order.getAmount()).isEqualByComparingTo(originalAmount); // Não deve aplicar desconto

		verify(authenticatedUserGateway).getCreationDate();
	}

	@Test @DisplayName("Deve lançar exceção quando a data de criação do usuário for nula")
	void shouldThrowExceptionWhenUserCreationDateIsNull() {
		// Arrange
		var order = OrderFixture.createValidOrder();

		when(authenticatedUserGateway.getCreationDate()).thenReturn(null);

		// Act & Assert
		assertThatThrownBy(() -> ApplyDiscountUseCase.applyDiscount(order, authenticatedUserGateway))
				.isInstanceOf(OrderException.class)
				.hasMessage("A data de criação do usuário informada para cálculo de desconto não pode ser nula");

		verify(authenticatedUserGateway).getCreationDate();
	}

	@Test @DisplayName("Deve aplicar desconto de 2% para usuário com 1 ano de cadastro")
	void shouldApply2PercentDiscountForUserWith1YearOfRegistration() {
		// Arrange
		var order = OrderFixture.createValidOrder();
		var originalAmount = order.getAmount(); // 51.80

		when(authenticatedUserGateway.getCreationDate()).thenReturn(OffsetDateTime.now().minusYears(1));

		// Act
		ApplyDiscountUseCase.applyDiscount(order, authenticatedUserGateway);

		// Assert
		assertThat(order.getAmount()).isLessThan(originalAmount);
		// Com 2% de desconto: 51.80 - (51.80 * 0.02) = 50.764
		assertThat(order.getAmount()).isEqualByComparingTo(new BigDecimal("50.7640"));

		verify(authenticatedUserGateway).getCreationDate();
	}
}
