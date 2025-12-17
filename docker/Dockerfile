FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar arquivos de dependências e build
COPY spotless-formatter.xml ./
COPY gradle/ ./gradle/
COPY gradlew build.gradle settings.gradle ./
RUN chmod +x ./gradlew

# Download de dependências
RUN --mount=type=secret,id=gh_user \
    --mount=type=secret,id=gh_token \
    export FOODCORE_SHARED_REPO_USERNAME=$(cat /run/secrets/gh_user) && \
    export FOODCORE_SHARED_REPO_TOKEN=$(cat /run/secrets/gh_token) && \
    ./gradlew dependencies --no-daemon

# Copiar código fonte
COPY src/ ./src/

# Corrigir erros de formatação
RUN --mount=type=secret,id=gh_user \
    --mount=type=secret,id=gh_token \
    export FOODCORE_SHARED_REPO_USERNAME=$(cat /run/secrets/gh_user) && \
    export FOODCORE_SHARED_REPO_TOKEN=$(cat /run/secrets/gh_token) && \
    ./gradlew :spotlessApply

# Construir o aplicativo
RUN --mount=type=secret,id=gh_user \
    --mount=type=secret,id=gh_token \
    export FOODCORE_SHARED_REPO_USERNAME=$(cat /run/secrets/gh_user) && \
    export FOODCORE_SHARED_REPO_TOKEN=$(cat /run/secrets/gh_token) && \
    ./gradlew build -x test --no-daemon

# Imagem final
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Atualizar ferramentas básicas para health checks
RUN apk add --no-cache curl wget tzdata

# Definir timezone para GMT-3 (exemplo America/Sao_Paulo)
RUN cp /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime && \
    echo "America/Sao_Paulo" > /etc/timezone

# Adicionar usuário não-root para segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copiar JAR construído
COPY --from=build /app/build/libs/*.jar app.jar

# Mudar para usuário não-root
USER appuser

# Porta da aplicação (modificável via docker-compose)
ARG APP_PORT
EXPOSE ${APP_PORT}

# Parâmetros para Monitoramento
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+UseStringDeduplication -Djava.security.egd=file:/dev/./urandom"

# Verificação de saúde interna
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget --spider -q http://localhost:${APP_PORT}/api/actuator/health || exit 1

# Iniciar aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]