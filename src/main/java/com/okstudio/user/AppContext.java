package com.okstudio.user;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.okstudio.user.dao.UserDao;
import com.okstudio.user.service.DummyMailSender;
import com.okstudio.user.service.UserService;
import com.okstudio.user.service.UserServiceTest.TestUserServiceImpl;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages="com.okstudio.user")
@Import(SqlServiceContext.class)
@PropertySource("/database.properties")
public class AppContext implements SqlMapConfig{
//	@Autowired UserDao userDao;
	
	@Autowired Environment env;
	
//	@Value("${db.driverClass}") Class<? extends Driver> driverClass;
//	@Value("${db.url}") String url;
//	@Value("${db.username}") String username;
//	@Value("${db.password}") String password;
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		try {
			dataSource.setDriverClass((Class<? extends java.sql.Driver>)Class.forName(env.getProperty("db.driverClass")));
		}
		catch(ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		dataSource.setUrl(env.getProperty("db.url"));
		dataSource.setUsername(env.getProperty("db.username"));
		dataSource.setPassword(env.getProperty("db.password"));
		
//		dataSource.setDriverClass(this.driverClass);
//		dataSource.setUrl(this.url);
//		dataSource.setUsername(this.username);
//		dataSource.setPassword(this.password);
		
		return dataSource;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource());
		return transactionManager;
	}
	
	@Override
	public Resource getSqlMapResource() {
		return new ClassPathResource("sqlmap.xml", UserDao.class);
	}
	
//	@Bean
//	public SqlMapConfig sqlMapConfig() {
//		return new UserSqlMapConfig();
//	}
	
	@Bean
	public static PropertyPlaceholderConfigurer placeholderConfigure() {
		return new PropertyPlaceholderConfigurer();
	}
	
	@Configuration
	@Profile("production")
	public static class ProductionAppContext {
		@Bean
		public MailSender mailSender() {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost("localhost");
			return mailSender;
		}
	}
	
	@Configuration
	@Profile("test")
	public static class TestAppContext {
		
		@Bean
		public UserService testUserService() {		
			return new TestUserServiceImpl();
		}
		
		@Bean
		public MailSender mailSender() {
			return new DummyMailSender();
		}
	}

}
