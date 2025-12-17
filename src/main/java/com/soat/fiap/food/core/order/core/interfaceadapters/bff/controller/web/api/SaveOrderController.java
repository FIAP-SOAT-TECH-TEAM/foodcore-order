package com.soat.fiap.food.core.order.core.interfaceadapters.bff.controller.web.api;

import com.soat.fiap.food.core.order.core.application.inputs.mappers.CreateOrderMapper;
import com.soat.fiap.food.core.order.core.application.usecases.ApplyDiscountUseCase;
import com.soat.fiap.food.core.order.core.application.usecases.CreateOrderUseCase;
import com.soat.fiap.food.core.order.core.application.usecases.EnsureValidOrderItemsUseCase;
import com.soat.fiap.food.core.order.core.application.usecases.PublishOrderCreatedEventUseCase;
import com.soat.fiap.food.core.order.core.interfaceadapters.bff.presenter.web.api.OrderPresenter;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.CatalogGateway;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.EventPublisherGateway;
import com.soat.fiap.food.core.order.core.interfaceadapters.gateways.OrderGateway;
import com.soat.fiap.food.core.order.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.EventPublisherSource;
import com.soat.fiap.food.core.order.infrastructure.common.source.OrderDataSource;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.request.CreateOrderRequest;
import com.soat.fiap.food.core.order.infrastructure.in.web.api.dto.response.OrderResponse;
import com.soat.fiap.food.core.shared.core.interfaceadapters.gateways.AuthenticatedUserGateway;
import com.soat.fiap.food.core.shared.infrastructure.common.source.AuthenticatedUserSource;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller: Salvar pedido.
 */
@Slf4j
public class SaveOrderController {

	/**
	 * Salva um pedido.
	 *
	 * @param createOrderRequest
	 *            Pedido a ser salvo
	 * @param orderDataSource
	 *            Origem de dados para o gateway de pedido
	 * @param catalogDatasource
	 *            Origem de dados para o gateway de produto
	 * @param authenticatedUserSource
	 *            Origem de dados para o gateway de usuário autenticado
	 * @param eventPublisherSource
	 *            Origem de publicação de eventos
	 * @return Pedido salvo com identificadores atualizados
	 */
	public static OrderResponse saveOrder(CreateOrderRequest createOrderRequest, OrderDataSource orderDataSource,
			CatalogDataSource catalogDatasource, AuthenticatedUserSource authenticatedUserSource,
			EventPublisherSource eventPublisherSource) {

		var orderGateway = new OrderGateway(orderDataSource);
		var productGateway = new CatalogGateway(catalogDatasource);
		var eventPublisherGateway = new EventPublisherGateway(eventPublisherSource);
		var authenticatedUserGateway = new AuthenticatedUserGateway((authenticatedUserSource));

		var orderInput = CreateOrderMapper.toInput(createOrderRequest, authenticatedUserGateway);
		var order = CreateOrderUseCase.createOrder(orderInput);

		EnsureValidOrderItemsUseCase.ensureValidOrderItems(order.getOrderItems(), productGateway);
		ApplyDiscountUseCase.applyDiscount(order, authenticatedUserGateway);

		var savedOrder = orderGateway.save(order);

		PublishOrderCreatedEventUseCase.publishCreateOrderEvent(savedOrder, eventPublisherGateway);

		var saveOrderToResponse = OrderPresenter.toOrderResponse(savedOrder);

		log.info("Pedido {} criado com sucesso. Total: {}", savedOrder.getId(), savedOrder.getAmount());

		return saveOrderToResponse;
	}
}
