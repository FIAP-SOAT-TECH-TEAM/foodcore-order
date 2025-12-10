package integration.bdd.common.config;

import java.io.IOException;
import java.util.Objects;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.azure.ServiceBusEmulatorContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.localstack.LocalStackContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

import com.github.dockerjava.api.model.Capability;

import integration.bdd.common.fixture.LocalStackFixture;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Configuração base para testes de integração que utilizam múltiplos serviços
 * emulados via Testcontainers.
 *
 * <p>
 * Esta classe centraliza todos os contêineres necessários para testes de
 * integração, fornecendo um ambiente completamente isolado, estável e
 * reproduzível.
 * </p>
 *
 * <p>
 * <b>Serviços integrados:</b>
 * </p>
 * <ul>
 * <li><b>PostgreSQL</b> — banco principal usado pelos serviços testados.</li>
 * <li><b>SQL Server</b> — dependência obrigatória para o Azure Service Bus
 * Emulator.</li>
 * <li><b>Azure Service Bus Emulator</b> — permite testar filas e tópicos
 * localmente.</li>
 * <li><b>LocalStack (Cognito)</b> — simula autenticação e gerenciamento de
 * usuários.</li>
 * <li><b>MailDev</b> — captura e exibe e-mails enviados pelo sistema durante os
 * testes.</li>
 * </ul>
 *
 * <p>
 * <b>Recursos relacionados:</b>
 * </p>
 * <ul>
 * <li><a href="https://testcontainers.com/modules/postgresql/">Testcontainers
 * PostgreSQL</a></li>
 * <li><a href="https://github.com/Azure/azure-service-bus-emulator">Azure
 * Service Bus Emulator - GitHub</a></li>
 * <li><a href="https://docs.localstack.cloud/">LocalStack
 * Documentation</a></li>
 * <li><a href=
 * "https://github.com/maildev/maildev/blob/master/docs/rest.md">MailDev REST
 * API</a></li>
 * </ul>
 *
 * @see <a href=
 *      "https://docs.spring.io/spring-boot/reference/testing/testcontainers.html">
 *      Spring Boot — Testcontainers Integration</a>
 */
@Testcontainers
public abstract class TestContainersConfiguration {

	private static final Dotenv dotenv = Dotenv.load();
	private static final Network network = Network.newNetwork();

	protected static String cognitoUserPoolId;
	static String cognitoEndpoint;

	private static final int mailDevSmtpExposedPort = 1025;
	private static final int mailDevApiExposedPort = 1080;
	static int mailDevSmtpHostPort;
	protected static int mailDevApiHostPort;

	static String azSvcBusConnectionString;

	@Container @ServiceConnection
	private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:16-alpine");

	@Container
	private static final MSSQLServerContainer<?> mssqlContainer = new MSSQLServerContainer<>(
			"mcr.microsoft.com/mssql/server:2022-CU14-ubuntu-22.04").acceptLicense()
			.withPassword("yourStrong(!)Password")
			.withCreateContainerCmdModifier(
					cmd -> Objects.requireNonNull(cmd.getHostConfig()).withCapAdd(Capability.SYS_PTRACE))
			.withNetwork(network);

	@Container
	private static final ServiceBusEmulatorContainer serviceBusContainer = new ServiceBusEmulatorContainer(
			"mcr.microsoft.com/azure-messaging/servicebus-emulator:1.0.1").acceptLicense()
			.withConfig(MountableFile.forClasspathResource("/azsvcbus/config.json"))
			.withNetwork(network)
			.withMsSqlServerContainer(mssqlContainer)
			.dependsOn(mssqlContainer);

	@Container
	protected static final LocalStackContainer localstackContainer = new LocalStackContainer(
			"localstack/localstack-pro:latest")
			.withServices("cognito-idp")
			.withEnv("LOCALSTACK_AUTH_TOKEN",
					System.getenv("LOCALSTACK_AUTH_TOKEN") != null ? System.getenv("LOCALSTACK_AUTH_TOKEN")
							: dotenv.get("LOCALSTACK_AUTH_TOKEN"))
			.withEnv("LOCALSTACK_EDGE_PORT", "4566")
			.withEnv("LOCALSTACK_REGION", "us-east-1")
			.withEnv("AWS_ACCESS_KEY_ID", "test")
			.withEnv("AWS_SECRET_ACCESS_KEY", "test");

	@Container
	private static final GenericContainer<?> mailDevContainer = new GenericContainer<>("maildev/maildev:latest")
			.withExposedPorts(mailDevSmtpExposedPort, mailDevApiExposedPort);

	static {
		postgreSQLContainer.start();
		mssqlContainer.start();
		serviceBusContainer.start();
		localstackContainer.start();
		mailDevContainer.start();

		azSvcBusConnectionString = serviceBusContainer.getConnectionString();
		mailDevSmtpHostPort = mailDevContainer.getMappedPort(mailDevSmtpExposedPort);
		mailDevApiHostPort = mailDevContainer.getMappedPort(mailDevApiExposedPort);
		cognitoEndpoint = localstackContainer.getEndpoint().toString();

		try {
			cognitoUserPoolId = LocalStackFixture.createUserPool(localstackContainer);
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException("Falha ao criar UserPool (Cognito) no LocalStack", e);
		}
	}
}
