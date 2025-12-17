package com.soat.fiap.food.core.order.infrastructure.in.event.listener.azsvcbus.order.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soat.fiap.food.core.order.core.interfaceadapters.dto.events.OrderReadyEventDto;
import com.soat.fiap.food.core.shared.infrastructure.common.source.EmailDataSource;
import com.soat.fiap.food.core.shared.infrastructure.common.source.UserSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler responsável por processar eventos de pedido pronto.
 *
 * <p>
 * Quando um pedido é marcado como "pronto", este handler envia um e-mail para o
 * usuário autenticado, notificando que seu pedido está disponível.
 * </p>
 */
@Slf4j @Service @RequiredArgsConstructor
public class OrderReadyHandler {

	private final EmailDataSource mailDataSource;
	private final UserSource userSource;
	/**
	 * Processa o evento de pedido pronto e envia uma notificação por e-mail.
	 *
	 * @param event
	 *            evento contendo informações do pedido pronto
	 */
	@Transactional
	public void handle(OrderReadyEventDto event) {
		log.info("Evento de pedido pronto recebido: {}", event.getOrderNumber());

		var client = userSource.getUserById(event.getClientId());

		if (client != null && !client.getEmail().isEmpty() && !client.getName().isEmpty()) {
			var subject = String.format("🍔 Seu pedido #%s está pronto!", event.getOrderNumber());

			var body = String.format(
					"<div style='font-family:Arial,sans-serif; color:#333;'>"
							+ "<h2 style='color:#FF5722;'>🍔 Olá %s!</h2>"
							+ "<p>Seu pedido <b>#%s</b> está pronto para retirada!</p>"
							+ "<p><b>Valor do pedido:</b> R$ %.2f</p>" + "<p><b>Pronto às:</b> %s</p>"
							+ "<hr style='border:none; border-top:1px solid #eee;'/>"
							+ "<p>Equipe <b>Food Core</b> agradece sua preferência! 👨‍🍳</p>" + "</div>",
					client.getName(), event.getOrderNumber(), event.getAmount(), event.getReadyAt());

			try {
				mailDataSource.sendEmail(client.getEmail(), subject, body);
				log.info("E-mail de notificação enviado com sucesso para o pedido: {}", event.getOrderNumber());
			} catch (Exception e) {
				log.error("Erro ao enviar e-mail para o pedido pronto: {}", event.getOrderNumber(), e);
			}
		}
	}
}
