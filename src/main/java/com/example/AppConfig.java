package com.example;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;

@Configuration
public class AppConfig {
	@Autowired
	DataSourceProperties dataSourceProperties;
	DataSource dataSource;
	
	@Bean
	DataSource realDataSource() {
		DataSourceBuilder factory = DataSourceBuilder
				.create(this.dataSourceProperties.getClassLoader())
				.url(this.dataSourceProperties.getUrl())
				.username(this.dataSourceProperties.getUsername())
				.password(this.dataSourceProperties.getPassword());
		this.dataSource = factory.build();
		return this.dataSource;
	}
	
	@Bean
	@Primary
	DataSource dataSource() {
		return new Log4jdbcProxyDataSource(this.dataSource);
	}
	// 이거 안해주면 한글 깨진다는데 멀쩡히 잘 나옴
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Bean
	CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		return filter;
	}
}
