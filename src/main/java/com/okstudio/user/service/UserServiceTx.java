package com.okstudio.user.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.okstudio.user.domain.User;

public class UserServiceTx implements UserService {
	private UserService userService;
	private PlatformTransactionManager transactionManager;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public void add(User user) {
		this.userService.add(user);
	}

	@Override
	public void upgradeLevels() {
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			this.userService.upgradeLevels();
			
			this.transactionManager.commit(status);			
		} catch(RuntimeException e) {
			this.transactionManager.rollback(status);
			throw e;
		}

	}

}
