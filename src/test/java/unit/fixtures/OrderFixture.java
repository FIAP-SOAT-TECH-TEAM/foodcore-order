package unit.fixtures;

import java.math.BigDecimal;
import java.util.List;

import com.soat.fiap.food.core.order.core.application.inputs.CreateOrderInput;
import com.soat.fiap.food.core.order.core.application.inputs.OrderItemInput;
import com.soat.fiap.food.core.order.core.domain.model.Order;
import com.soat.fiap.food.core.order.core.domain.model.OrderItem;
import com.soat.fiap.food.core.order.core.domain.vo.OrderItemPrice;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.CreateOrderRequest;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.OrderItemRequest;

/**
 * Fixture utilitária para criação de objetos relacionados ao módulo Order
 * utilizados em testes unitários.
 * <p>
 * Inclui métodos para gerar entidades, value objects, inputs e requests usados
 * em diferentes cenários de teste.
 */
public class OrderFixture {

	/**
	 * Cria uma instância de {@link OrderItemPrice} com quantidade fixa igual a 1.
	 *
	 * @param unitPrice
	 *            preço unitário do item
	 * @return instância de {@link OrderItemPrice}
	 */
	private static OrderItemPrice price(BigDecimal unitPrice) {
		return new OrderItemPrice(1, unitPrice);
	}

	/**
	 * Cria uma instância de {@link OrderItemPrice} com quantidade e preço
	 * definidos.
	 *
	 * @param quantity
	 *            quantidade do item
	 * @param unitPrice
	 *            preço unitário
	 * @return instância configurada de {@link OrderItemPrice}
	 */
	private static OrderItemPrice price(int quantity, BigDecimal unitPrice) {
		return new OrderItemPrice(quantity, unitPrice);
	}

	/**
	 * Retorna um preço válido padrão para hambúrgueres.
	 *
	 * @return preço padrão de hambúrguer
	 */
	private static OrderItemPrice validBurgerPrice() {
		return new OrderItemPrice(2, new BigDecimal("25.90"));
	}

	/**
	 * Retorna o preço de uma bebida.
	 *
	 * @return preço de bebida
	 */
	private static OrderItemPrice beveragePrice() {
		return new OrderItemPrice(1, new BigDecimal("8.50"));
	}

	/**
	 * Retorna o preço de uma sobremesa.
	 *
	 * @return preço de sobremesa
	 */
	private static OrderItemPrice dessertPrice() {
		return new OrderItemPrice(1, new BigDecimal("18.90"));
	}

	/**
	 * Retorna um preço alto usado para cenários de regra de negócio.
	 *
	 * @return preço elevado
	 */
	private static OrderItemPrice expensivePrice() {
		return new OrderItemPrice(1, new BigDecimal("45.00"));
	}

	/**
	 * Cria um pedido válido contendo um único item padrão.
	 *
	 * @return instância válida de {@link Order}
	 */
	public static Order createValidOrder() {
		var orderItems = List.of(createValidOrderItem());
		return new Order("A23basb3u123", orderItems);
	}

	/**
	 * Cria um pedido contendo múltiplos itens (hambúrguer, bebida e sobremesa).
	 *
	 * @return instância de {@link Order} com vários itens
	 */
	public static Order createOrderWithMultipleItems() {
		var orderItems = List.of(createValidOrderItem(), createBeverageOrderItem(), createDessertOrderItem());
		return new Order("A23basb3u123", orderItems);
	}

	/**
	 * Cria um pedido sem usuário associado.
	 * <p>
	 * Usado para validar regras de obrigatoriedade.
	 */
	public static void createOrderWithoutUser() {
		var orderItems = List.of(createValidOrderItem());
		new Order(null, orderItems);
	}

	/**
	 * Cria um item de pedido válido (hambúrguer).
	 *
	 * @return instância de {@link OrderItem}
	 */
	public static OrderItem createValidOrderItem() {
		return new OrderItem(1L, "Big Mac", validBurgerPrice(), null);
	}

	/**
	 * Cria um item de bebida.
	 *
	 * @return instância de {@link OrderItem}
	 */
	public static OrderItem createBeverageOrderItem() {
		return new OrderItem(2L, "Coca-Cola", beveragePrice(), "Sem gelo");
	}

	/**
	 * Cria um item de sobremesa.
	 *
	 * @return instância de {@link OrderItem}
	 */
	public static OrderItem createDessertOrderItem() {
		return new OrderItem(3L, "Torta de Chocolate", dessertPrice(), "Aquecida");
	}

	/**
	 * Cria um item com preço caro utilizado para validações específicas.
	 *
	 * @return instância de {@link OrderItem}
	 */
	public static OrderItem createExpensiveOrderItem() {
		return new OrderItem(4L, "Combo Premium", expensivePrice(), null);
	}

	/**
	 * Cria um item de pedido customizado com preço unitário simples.
	 *
	 * @param name
	 *            nome do produto
	 * @param id
	 *            ID do item
	 * @param priceValue
	 *            preço unitário
	 * @return item de pedido configurado
	 */
	public static OrderItem createOrderItem(String name, Long id, BigDecimal priceValue) {
		return new OrderItem(id, name, price(priceValue), "Sem gelo");
	}

	/**
	 * Cria um item customizado com quantidade customizável.
	 *
	 * @param name
	 *            nome do produto
	 * @param id
	 *            ID do item
	 * @param priceValue
	 *            preço unitário
	 * @param quantity
	 *            quantidade
	 * @return item configurado
	 */
	public static OrderItem createOrderItem(String name, Long id, BigDecimal priceValue, int quantity) {
		return new OrderItem(id, name, price(quantity, priceValue), "Sem gelo");
	}

	/**
	 * Cria um item com observações específicas.
	 *
	 * @return item configurado com observação
	 */
	public static OrderItem createOrderItemWithObservations() {
		return new OrderItem(2L, "Batata Frita G", price(new BigDecimal("15.00")), "Sem sal");
	}

	/**
	 * Cria um pedido com apenas um item.
	 *
	 * @return pedido contendo um único item
	 */
	public static Order createOrderWithSingleItem() {
		var orderItems = List.of(createValidOrderItem());
		return new Order("A23basb3u123", orderItems);
	}

	/**
	 * Cria um {@link CreateOrderInput} válido com item padrão.
	 *
	 * @return instância válida de {@link CreateOrderInput}
	 */
	public static CreateOrderInput createValidCreateOrderInput() {
		return new CreateOrderInput("A23basb3u123", List.of(createValidOrderItemInput()));
	}

	/**
	 * Cria um {@link CreateOrderInput} com vários itens.
	 *
	 * @return entrada de criação de pedido com múltiplos itens
	 */
	public static CreateOrderInput createCreateOrderInputWithMultipleItems() {
		var orderItems = List.of(createValidOrderItemInput(), createBeverageOrderItemInput(),
				createDessertOrderItemInput());
		return new CreateOrderInput("A23basb3u123", orderItems);
	}

	/**
	 * Cria um item de pedido válido para input.
	 *
	 * @return instância de {@link OrderItemInput}
	 */
	public static OrderItemInput createValidOrderItemInput() {
		return new OrderItemInput(1L, "Big Mac", 2, new BigDecimal("25.90"), null);
	}

	/**
	 * Cria item input representando bebida.
	 *
	 * @return item input de bebida
	 */
	public static OrderItemInput createBeverageOrderItemInput() {
		return new OrderItemInput(2L, "Coca-Cola", 1, new BigDecimal("8.50"), "Sem gelo");
	}

	/**
	 * Cria item input de sobremesa.
	 *
	 * @return item input de sobremesa
	 */
	public static OrderItemInput createDessertOrderItemInput() {
		return new OrderItemInput(3L, "Torta de Chocolate", 1, new BigDecimal("18.90"), "Aquecida");
	}

	/**
	 * Cria um {@link OrderItemRequest} válido.
	 *
	 * @return requisição válida de item
	 */
	public static OrderItemRequest createValidOrderItemRequest() {
		return new OrderItemRequest(1L, "X-Burger", 2, new BigDecimal("22.90"), "Sem cebola");
	}

	/**
	 * Cria uma {@link CreateOrderRequest} válida com um item.
	 *
	 * @return requisição válida de criação de pedido
	 */
	public static CreateOrderRequest createValidCreateOrderRequest() {
		return new CreateOrderRequest(List.of(createValidOrderItemRequest()));
	}

}
