package com.okstudio.user.service;

import java.util.List;

import com.okstudio.user.dao.UserDao;
import com.okstudio.user.domain.Level;
import com.okstudio.user.domain.User;

public class UserService {
	public static final int MIN_LOGCOUNT_FOL_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		for(User user : users) {
			if(this.canUpgradeLevel(user)) {
				this.upgradeLevel(user);
			}
		}
	}

	public void add(User user) {
		if(user.getLevel() == null) {
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);		
	}
	
	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch(currentLevel) {
			case BASIC : return (user.getLogin() >= MIN_LOGCOUNT_FOL_SILVER);
			case SILVER : return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
			case GOLD : return false;
			default : throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}
	
	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		this.userDao.update(user);
	}
}
