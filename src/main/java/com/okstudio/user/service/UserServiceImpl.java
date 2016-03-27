package com.okstudio.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.okstudio.user.dao.UserDao;
import com.okstudio.user.domain.Level;
import com.okstudio.user.domain.User;

@Service("userService")
public class UserServiceImpl implements UserService {
	public static final int MIN_LOGCOUNT_FOL_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	@Autowired private UserDao userDao;
	@Autowired private MailSender mailSender;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
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
	
	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		this.userDao.update(user);
		
		this.sendUpgradeEmail(user);
	}
	
	private void sendUpgradeEmail(User user) {		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("DoNotReply@ksug.org");
		mailMessage.setSubject("upgrade 안내");
		mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다.");
		
		this.mailSender.send(mailMessage);
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


	@Override
	public User get(String id) {
		return this.userDao.get(id);
	}

	@Override
	public List<User> getAll() {
		return this.userDao.getAll();
	}

	@Override
	public void deleteAll() {
		this.userDao.deleteAll();
	}

	@Override
	public void update(User user) {
		this.userDao.update(user);		
	}
}
