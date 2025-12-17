package integration.bdd.common.fixture;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.localstack.LocalStackContainer;

/**
 * Classe utilitária responsável por preparar dados de teste no LocalStack,
 * criando pools e usuários Cognito de forma programática para os testes de
 * integração.
 */
public class LocalStackFixture {

	private static final Logger log = LoggerFactory.getLogger(LocalStackFixture.class);

	/**
	 * Cria um novo User Pool Cognito no container LocalStack informado.
	 *
	 * @param localStackContainer
	 *            container ativo do LocalStack
	 * @return o ID do User Pool criado
	 * @throws IOException
	 *             se ocorrer erro de execução no container
	 * @throws InterruptedException
	 *             se a thread for interrompida durante a execução
	 */
	public static String createUserPool(LocalStackContainer localStackContainer)
			throws IOException, InterruptedException {

		log.debug("-> Criando User Pool no LocalStack...");

		var createUserPoolResult = localStackContainer.execInContainer("awslocal", "cognito-idp", "create-user-pool",
				"--pool-name", "test-user-pool", "--schema",
				"[{\"Name\":\"cpf\",\"AttributeDataType\":\"String\",\"Mutable\":true},"
						+ "{\"Name\":\"role\",\"AttributeDataType\":\"String\",\"Mutable\":true},"
						+ "{\"Name\":\"createdAt\",\"AttributeDataType\":\"String\",\"Mutable\":true}]",
				"--query", "UserPool.Id", "--output", "text");

		var userPoolId = createUserPoolResult.getStdout().trim();
		log.debug("-> User Pool criado: {}", userPoolId);

		return userPoolId;
	}

	/**
	 * Cria um usuário de teste no User Pool Cognito especificado.
	 *
	 * @param localStackContainer
	 *            container ativo do LocalStack
	 * @param userPoolId
	 *            ID do User Pool Cognito onde o usuário será criado
	 * @param email
	 *            endereço de e-mail do usuário de teste
	 * @param name
	 *            nome do usuário de teste
	 * @return o Subject ID (sub) do usuário criado
	 * @throws IOException
	 *             se ocorrer erro de execução no container
	 * @throws InterruptedException
	 *             se a thread for interrompida durante a execução
	 */
	public static String createUser(LocalStackContainer localStackContainer, String userPoolId, String email,
			String name) throws IOException, InterruptedException {

		log.debug("-> Criando usuário de teste no User Pool {}...", userPoolId);

		var createUserResult = localStackContainer.execInContainer("awslocal", "cognito-idp", "admin-create-user",
				"--user-pool-id", userPoolId, "--username", "testuser", "--user-attributes",
				"Name=email,Value=" + email, "Name=name,Value=" + name, "Name=custom:cpf,Value=866.756.240-83",
				"Name=custom:role,Value=CUSTOMER", "Name=custom:createdAt,Value=2025-10-07T02:00:00Z", "--query",
				"User.Attributes[?Name=='sub'].Value | [0]", "--output", "text");

		var sub = createUserResult.getStdout().trim();
		log.debug("-> Usuário criado com sub: {}", sub);

		return sub;
	}

}
