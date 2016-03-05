package com.okstudio.user.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class DaoFactory {
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource ();
		
		Class driverClass = null;
		try {
			driverClass = Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataSource.setDriverClass(driverClass);
		//dataSource.setDriverClass(com.mar);
		dataSource.setUrl("jdbc:mysql://localhost/springbook?characterEncoding=UTF-8");
		dataSource.setUsername("spring");
		dataSource.setPassword("book");

		return dataSource;
	}

	@Bean
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setDataSource(dataSource());
		return userDao;
	}
}