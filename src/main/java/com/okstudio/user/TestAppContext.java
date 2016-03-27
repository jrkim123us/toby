package com.okstudio.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import com.okstudio.user.service.DummyMailSender;
import com.okstudio.user.service.UserService;
import com.okstudio.user.service.UserServiceTest.TestUserServiceImpl;

@Configuration
public class TestAppContext {
	
	@Bean
	public UserService testUserService() {		
		return new TestUserServiceImpl();
	}
	
	@Bean
	public MailSender mailSender() {
		return new DummyMailSender();
	}
}
