package com.example.demo.web.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.sql.init.DatabaseInitializationMode;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DataSourceProperties.class)
@Slf4j
public class DatabaseConfig {
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@Primary
	public DataSource dataSource(@Qualifier("dataSourceProperties") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().build();
	}

	@Bean
	@Primary
	public DataSourceTransactionManager transactionManager(Environment environment, DataSource dataSource,
			ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
		log.info("[!] transactionManager: dataSource={}", System.identityHashCode(dataSource));
		DataSourceTransactionManager transactionManager = environment
				.getProperty("spring.dao.exceptiontranslation.enabled", Boolean.class, Boolean.TRUE)
						? new JdbcTransactionManager(dataSource)
						: new DataSourceTransactionManager(dataSource);
		transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
		return transactionManager;
	}

	@Bean
	@Primary
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		log.info("[!] sqlSessionFactory: dataSource={}", System.identityHashCode(dataSource));
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		return (SqlSessionFactory) sqlSessionFactory.getObject();
	}

	@Bean
	public SqlDataSourceScriptDatabaseInitializer ddlOnlyScriptDataSourceInitializer(DataSource dataSource,
			DataSourceProperties properties) {
		log.info("[!] ddlOnlyScriptDataSourceInitializer: dataSource={}", System.identityHashCode(dataSource));
		DatabaseInitializationSettings settings = new DatabaseInitializationSettings();
		settings.setSchemaLocations(properties.getSchema());
		settings.setContinueOnError(properties.isContinueOnError());
		settings.setSeparator(properties.getSeparator());
		settings.setEncoding(properties.getSqlScriptEncoding());
		settings.setMode(DatabaseInitializationMode.EMBEDDED);
		return new SqlDataSourceScriptDatabaseInitializer(dataSource, settings);
	}

	@Bean
	@DependsOn("ddlOnlyScriptDataSourceInitializer")
	SqlDataSourceScriptDatabaseInitializer dmlOnlyScriptDataSourceInitializer(DataSource dataSource,
			DataSourceProperties properties) {
		log.info("[!] dmlOnlyScriptDataSourceInitializer: dataSource={}", System.identityHashCode(dataSource));
		DatabaseInitializationSettings settings = new DatabaseInitializationSettings();
		settings.setDataLocations(properties.getData());
		settings.setContinueOnError(properties.isContinueOnError());
		settings.setSeparator(properties.getSeparator());
		settings.setEncoding(properties.getSqlScriptEncoding());
		settings.setMode(DatabaseInitializationMode.EMBEDDED);
		return new SqlDataSourceScriptDatabaseInitializer(dataSource, settings);
	}
}
