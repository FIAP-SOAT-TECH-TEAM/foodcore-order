package integration.bdd.common.steps;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import integration.bdd.common.config.CucumberSpringConfiguration;
import io.cucumber.java.pt.Dado;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * Classe responsável por definir Steps comuns relacionados a Seed de banco de
 * dados
 */
public class SeedSteps extends CucumberSpringConfiguration {

	private static final Logger log = LoggerFactory.getLogger(SeedSteps.class);

	@Autowired
	private DataSource dataSource;

	/**
	 * Cria estrutura de tabelas e insere produtos utilizando Liquibase.
	 *
	 * @throws Exception
	 *             se ocorrer algum erro na migração.
	 */
	@Dado("que existam pedidos")
	public void queExistamPedidos() throws Exception {
		log.debug("Inicializando banco de dados (DDL e DML)");

		var connection = DataSourceUtils.getConnection(dataSource);
		var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
		var liquibase = new Liquibase("db/changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor(),
				database);

		liquibase.update(new Contexts("local,dev"), new LabelExpression());

		connection.close();
	}
}
