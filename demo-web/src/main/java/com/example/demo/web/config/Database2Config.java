package com.example.demo.web.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.datasource2", name = "url")
@EnableConfigurationProperties(DataSourceProperties.class)
@Slf4j
public class Database2Config {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource2")
	public DataSourceProperties dataSourceProperties2() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource dataSource2(@Qualifier("dataSourceProperties2") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().build();
	}

	@Bean
	public DataSourceTransactionManager transactionManager2(Environment environment, @Qualifier("dataSource2") DataSource dataSource,
			ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
		log.info("[!] transactionManager2: dataSource={}", System.identityHashCode(dataSource));
		DataSourceTransactionManager transactionManager = environment
				.getProperty("spring.dao.exceptiontranslation.enabled", Boolean.class, Boolean.TRUE)
						? new JdbcTransactionManager(dataSource)
						: new DataSourceTransactionManager(dataSource);
		transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
		return transactionManager;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory2(@Qualifier("dataSource2") DataSource dataSource) throws Exception {
		log.info("[!] sqlSessionFactory2: dataSource={}", System.identityHashCode(dataSource));
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		return (SqlSessionFactory) sqlSessionFactory.getObject();
	}

	@Bean
	public SqlDataSourceScriptDatabaseInitializer ddlOnlyScriptDataSourceInitializer2(@Qualifier("dataSource2") DataSource dataSource,
			DataSourceProperties properties) {
		log.info("[!] ddlOnlyScriptDataSourceInitializer2: dataSource={}", System.identityHashCode(dataSource));
		DatabaseInitializationSettings settings = new DatabaseInitializationSettings();
		settings.setSchemaLocations(properties.getSchema());
		settings.setContinueOnError(properties.isContinueOnError());
		settings.setSeparator(properties.getSeparator());
		settings.setEncoding(properties.getSqlScriptEncoding());
		settings.setMode(DatabaseInitializationMode.EMBEDDED);
		return new SqlDataSourceScriptDatabaseInitializer(dataSource, settings);
	}	

	@Bean
	@DependsOn("ddlOnlyScriptDataSourceInitializer2")
	SqlDataSourceScriptDatabaseInitializer dmlOnlyScriptDataSourceInitializer2(@Qualifier("dataSource2") DataSource dataSource,
			DataSourceProperties properties) {
		log.info("[!] dmlOnlyScriptDataSourceInitializer2: dataSource={}", System.identityHashCode(dataSource));
		DatabaseInitializationSettings settings = new DatabaseInitializationSettings();
		settings.setDataLocations(properties.getData());
		settings.setContinueOnError(properties.isContinueOnError());
		settings.setSeparator(properties.getSeparator());
		settings.setEncoding(properties.getSqlScriptEncoding());
		settings.setMode(DatabaseInitializationMode.EMBEDDED);
		return new SqlDataSourceScriptDatabaseInitializer(dataSource, settings);
	}

}
