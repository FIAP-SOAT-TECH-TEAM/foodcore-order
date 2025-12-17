package com.soat.fiap.food.core.order.infrastructure.in.web.api.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuração do SpringDoc para documentação da API
 */
@Configuration
public class SpringDocConfig {

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("bearer-key",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
				.info(new Info().title("Food Core API - Order")
						.description(
								"Microsserviço de gerenciamento de pedidos, implementado com Clean Architecture e DDD.")
						.version("1.0.0")
						.contact(new Contact().name("Equipe FIAP/SOAT")
								.email("suporte@foodcoreapi.com")
								.url("https://github.com/FIAP-SOAT-TECH-TEAM/foodcore-api"))
						.license(new License().name("Apache 2.0")
								.url("https://www.apache.org/licenses/LICENSE-2.0.html")))
				.servers(List.of(new Server().url(contextPath).description("API Server")));
	}

	@Bean
	public GroupedOpenApi orderApi() {
		return GroupedOpenApi.builder()
				.group("pedidos")
				.packagesToScan("com.soat.fiap.food.core.api.order")
				.pathsToMatch("/orders/**")
				.build();
	}

}
