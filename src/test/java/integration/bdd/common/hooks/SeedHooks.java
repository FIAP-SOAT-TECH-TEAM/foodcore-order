package integration.bdd.common.hooks;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import integration.bdd.common.config.CucumberSpringConfiguration;
import io.cucumber.java.After;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * Classe responsável por definir Hooks comuns relacionados a Seed de banco de
 * dados
 */
public class SeedHooks extends CucumberSpringConfiguration {

	private static final Logger log = LoggerFactory.getLogger(SeedHooks.class);

	@Autowired
	private DataSource dataSource;

	/**
	 * Hook executado antes de cada cenário Cucumber. Remove todos os registros da
	 * base de dados para garantir um estado limpo.
	 */
	@After
	public void limparBanco() throws DatabaseException, SQLException {
		log.debug("Limpando banco de dados (DDL e DML)");

		var connection = DataSourceUtils.getConnection(dataSource);
		var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
		var liquibase = new Liquibase("db/changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor(),
				database);

		liquibase.dropAll();

		connection.close();
	}
}
