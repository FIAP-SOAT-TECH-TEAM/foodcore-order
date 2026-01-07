# 🛒 FoodCore Order

<div align="center">

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_foodcore-order&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_foodcore-order)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_foodcore-order&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_foodcore-order)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_foodcore-order&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_foodcore-order)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_foodcore-order&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_foodcore-order)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_foodcore-order&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_foodcore-order)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_foodcore-order&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_foodcore-order)

</div>

Microsserviço responsável pelo gerenciamento de pedidos do sistema FoodCore, incluindo criação, acompanhamento de status e comunicação com outros microsserviços. Desenvolvido como parte do curso de Arquitetura de Software da FIAP (Tech Challenge).

<div align="center">
  <a href="#visao-geral">Visão Geral</a> •
  <a href="#arquitetura">Arquitetura</a> •
  <a href="#infra">Infraestrutura</a> •
  <a href="#tecnologias">Tecnologias</a> •
  <a href="#debitos-tecnicos">Débitos Técnicos</a> •
  <a href="#diagramas">Diagramas</a> •
  <a href="#instalacao-e-uso">Instalação e Uso</a> •
  <a href="#apis">APIs</a> •
  <a href="#contribuicao">Contribuição</a>
</div><br>

> 📽️ Vídeo de demonstração da arquitetura: [https://www.youtube.com/watch?v=XgUpOKJjqak](https://www.youtube.com/watch?v=XgUpOKJjqak)<br>

---

<h2 id="visao-geral">📋 Visão Geral</h2>

O **FoodCore Order** é o microsserviço central do sistema, responsável por todo o ciclo de vida dos pedidos:

- **Criação de pedidos**: Recebe itens selecionados pelo cliente
- **Acompanhamento de status**: Gerencia estados (Recebido → Em Preparação → Pronto → Finalizado)
- **Comunicação assíncrona**: Publica eventos no Azure Service Bus para outros microsserviços
- **Notificação de clientes**: Envia atualizações sobre status do pedido
- **Integração síncrona**: Comunica-se com o microsserviço de catálogo via HTTP

Este microsserviço faz parte de uma arquitetura de microsserviços que segue os princípios de Clean Architecture e Domain-Driven Design.

### Principais Recursos

- **Criação de Pedidos**: Montagem de combos e pedidos avulsos
- **Gerenciamento de Status**: Workflow completo de status do pedido
- **Eventos de Domínio**: Publicação de eventos de mudança de status
- **Chargeback**: Estorno de pedidos cancelados
- **Comunicação Assíncrona**: Integração com Azure Service Bus

---

<h2 id="arquitetura">🧱 Arquitetura</h2>

<details>
<summary>Expandir para mais detalhes</summary>

O **FoodCore Order** segue os princípios de **Clean Architecture** e **Domain-Driven Design (DDD)**.

### 🎯 Princípios Adotados

- **Separação de responsabilidades**: Cada camada tem responsabilidade bem definida
- **Independência de frameworks**: Domínio não depende de Spring ou outras bibliotecas
- **Testabilidade**: Lógica de negócio isolada facilita testes unitários
- **Inversão de dependências**: Detalhes técnicos dependem do domínio
- **SAGA Coreografada**: Comunicação assíncrona via eventos

---

### 🔄 Fluxo de Pedidos

1. **Criação do Pedido**
   - Cliente seleciona itens do catálogo
   - Pedido é criado com status "PENDING"
   - Evento `OrderCreatedEvent` é publicado

2. **Pagamento Confirmado**
   - Recebe evento `PaymentApprovedEvent` do microsserviço de pagamento
   - Status atualizado para "RECEIVED"
   - Cliente é notificado

3. **Preparação**
   - Cozinha inicia preparação
   - Status atualizado para "IN_PREPARATION"

4. **Pedido Pronto**
   - Status atualizado para "READY"
   - Cliente é notificado para retirada

5. **Finalização**
   - Cliente retira o pedido
   - Status atualizado para "FINISHED"

---

### ⚙️ Camadas da Arquitetura

| Camada | Responsabilidade |
|--------|------------------|
| **Domínio** | Entidades (`Order`, `OrderItem`), Value Objects, Eventos de Domínio |
| **Aplicação** | Use Cases (`SaveOrderUseCase`, `UpdateOrderStatusUseCase`, `ChargebackOrderUseCase`) |
| **Interface** | Controllers REST, Presenters, Gateways |
| **Infraestrutura** | Persistência (PostgreSQL), Message Broker (Azure Service Bus), HTTP Client |

---

### 🏗️ Microsserviços do Ecossistema

| Microsserviço | Responsabilidade | Repositório |
|---------------|------------------|-------------|
| **foodcore-auth** | Autenticação (Azure Function + Cognito) | [foodcore-auth](https://github.com/FIAP-SOAT-TECH-TEAM/foodcore-auth) |
| **foodcore-order** | Gerenciamento de pedidos (este repositório) | [foodcore-order](https://github.com/FIAP-SOAT-TECH-TEAM/foodcore-order) |
| **foodcore-payment** | Processamento de pagamentos | [foodcore-payment](https://github.com/FIAP-SOAT-TECH-TEAM/foodcore-payment) |
| **foodcore-catalog** | Catálogo de produtos | [foodcore-catalog](https://github.com/FIAP-SOAT-TECH-TEAM/foodcore-catalog) |

</details>

---

<h2 id="infra">🌐 Infraestrutura</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### Recursos Kubernetes

| Recurso | Descrição |
|---------|-----------|
| **Deployment** | Pods com health probes, limites de recursos, variáveis de ambiente |
| **Service** | Exposição interna no cluster |
| **Ingress** | Roteamento via NGINX: `/api/orders/*` |
| **ConfigMap** | Configurações não sensíveis |
| **Secrets** | Credenciais criptografadas (Database, Service Bus) |
| **HPA** | Escalabilidade automática baseada em CPU/memória |

### Integrações

| Serviço | Tipo | Descrição |
|---------|------|-----------|
| **Azure Service Bus** | Assíncrona | Publicação/consumo de eventos |
| **PostgreSQL** | Síncrona | Persistência de dados |
| **FoodCore Catalog** | HTTP | Validação de produtos |

### Observabilidade

- **Logs**: Envio para Elasticsearch via Fluentd
- **Métricas**: Exposição para Prometheus via Micrometer
- **Tracing**: Instrumentação com Zipkin
- **Dashboards**: Visualização no Grafana

</details>

---

<h2 id="tecnologias">🔧 Tecnologias</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### Backend
- **Java 21**: Linguagem principal
- **Spring Boot 3.4**: Framework base
- **Spring Data JPA**: Persistência
- **MapStruct**: Mapeamento DTO ↔ Entidade
- **Lombok**: Redução de boilerplate
- **Retrofit**: Cliente HTTP

### Banco de Dados
- **PostgreSQL**: Banco relacional
- **Liquibase**: Migrations

### Mensageria
- **Azure Service Bus**: Comunicação assíncrona entre microsserviços

### Infraestrutura
- **Docker / Docker Compose**: Containerização
- **Kubernetes (AKS)**: Orquestração em produção
- **Helm**: Gerenciamento de pacotes K8s
- **Terraform**: IaC

### Qualidade
- **SonarCloud**: Análise estática
- **JUnit 5 + Mockito**: Testes unitários
- **Cucumber**: Testes BDD

</details>

---

<h2 id="debitos-tecnicos">⚠️ Débitos Técnicos</h2>

<details>
<summary>Expandir para mais detalhes</summary>


| Débito | Descrição | Impacto |
|--------|-----------|---------|
| **Separar Notificação** | Extrair responsabilidade de notificação para Azure Function com trigger de Azure Service Bus | Reduz acoplamento e melhora escalabilidade |
| **Transactional Outbox Pattern** | Implementar padrão para evitar escrita duplicada na SAGA coreografada | Garante consistência eventual |
| **Workload Identity** | Usar Workload Identity para Pods acessarem recursos Azure (atual: Azure Key Vault Provider) | Melhora segurança e gestão de credenciais |
| **OpenTelemetry** | Migrar de Zipkin/Micrometer para OpenTelemetry | Padronização de observabilidade |
| **WAF Layer** | Implementar camada WAF antes do API Gateway para proteção OWASP TOP 10 | Segurança adicional |

</details>

---

<h2 id="diagramas">📊 Diagramas</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### Fluxo de Criação de Pedido

![Eventos de domínio - Criação de Pedido](docs/diagrams/order-created.svg)

### Fluxo de Preparação e Entrega

![Eventos de domínio - Preparação e Entrega](docs/diagrams/order-preparing.svg)

</details>

---

<h2 id="instalacao-e-uso">🚀 Instalação e Uso</h2>

### Pré-requisitos
- Java 21
- Docker e Docker Compose
- Gradle

### Desenvolvimento Local

```bash
# Clonar repositório
git clone https://github.com/FIAP-SOAT-TECH-TEAM/foodcore-order.git
cd foodcore-order

# Subir dependências (PostgreSQL, etc.)
docker-compose -f docker/docker-compose.yml up -d

# Configurar variáveis de ambiente
cp env-example .env

# Executar aplicação
./gradlew bootRun --args='--spring.profiles.active=local'

# Executar testes
./gradlew test

# Executar testes BDD
./gradlew cucumber
```

---

<h2 id="apis">📡 APIs</h2>

### Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/orders` | Criar novo pedido |
| `GET` | `/api/orders/{id}` | Buscar pedido por ID |
| `PATCH` | `/api/orders/{id}/status` | Atualizar status do pedido |
| `POST` | `/api/orders/{id}/chargeback` | Estornar pedido |

### Documentação
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI**: `http://localhost:8080/v3/api-docs`

---

<h2 id="contribuicao">🤝 Contribuição</h2>

### Fluxo de Deploy

1. Abra um Pull Request com suas alterações
2. Pipeline CI executa testes e análise de código
3. Após aprovação de CODEOWNER, merge para `main`
4. Pipeline CD faz deploy automático no AKS

### Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

---

<div align="center">
  <strong>FIAP - Pós-graduação em Arquitetura de Software</strong><br>
  Tech Challenge
</div>
