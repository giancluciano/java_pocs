package com.example.simpleapi;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MyDataSourceConfiguration {
	@Bean
	@ConfigurationProperties(prefix = "app.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
}
