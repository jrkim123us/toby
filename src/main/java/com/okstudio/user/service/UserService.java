package com.okstudio.user.service;

import java.util.List;

import com.okstudio.user.dao.UserDao;
import com.okstudio.user.domain.Level;
import com.okstudio.user.domain.User;

public class UserService {
	UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		for(User user : users) {
			Boolean isChanged = null;
			if(user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
				user.setLevel(Level.SILVER);
				isChanged = true;
			} else if(user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
				user.setLevel(Level.GOLD);
				isChanged = true;
			} else if(user.getLevel() == Level.GOLD) {
				isChanged = false;
			} else {
				isChanged = false;
			}
			
			if(isChanged) {
				userDao.update(user);
			}
		}
	}

	public void add(User user) {
		if(user.getLevel() == null) {
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);		
	}
}
