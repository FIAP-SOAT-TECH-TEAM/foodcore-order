package com.soat.fiap.food.core.order.infrastructure.common.event.azsvcbus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

/**
 * Classe de configuração do Azure Service Bus.
 * <p>
 */
@Configuration
public class ServiceBusConfig {

	@Value("${azsvcbus.connection-string}")
	private String connectionString;

	/** Fila para eventos de pedido pronto. */
	public static final String ORDER_READY_QUEUE = "order.ready.queue";

	/** Nome do tópico para eventos de pedido criado. */
	public static final String ORDER_CREATED_TOPIC = "order.created.topic";

	/** Nome do tópico para eventos de pedido cancelado. */
	public static final String ORDER_CANCELED_TOPIC = "order.canceled.topic";

	/** Fila para eventos de pagamento aprovado. */
	public static final String PAYMENT_APPROVED_QUEUE = "payment.approved.queue";

	/** Fila para eventos de pagamento expirado. */
	public static final String PAYMENT_EXPIRED_QUEUE = "payment.expired.queue";

	/** Fila para eventos de estoque estornado. */
	public static final String STOCK_REVERSAL_QUEUE = "stock.reversal.queue";

	/** Nome da subscription para eventos de pedido criado. */
	public static final String CATALOG_ORDER_CREATED_TOPIC_SUBSCRIPTION = "catalog.order.created.topic.subscription";

	@Bean
	public ServiceBusClientBuilder serviceBusClientBuilder() {
		return new ServiceBusClientBuilder().connectionString(connectionString);
	}

	@Bean
	public ServiceBusSenderClient orderCreatedSender(ServiceBusClientBuilder builder) {
		return builder.sender().topicName(ORDER_CREATED_TOPIC).buildClient();
	}

	@Bean
	public ServiceBusSenderClient orderCanceledSender(ServiceBusClientBuilder builder) {
		return builder.sender().queueName(ORDER_CANCELED_TOPIC).buildClient();
	}

	@Bean
	public ServiceBusSenderClient orderReadySender(ServiceBusClientBuilder builder) {
		return builder.sender().queueName(ORDER_READY_QUEUE).buildClient();
	}
}
