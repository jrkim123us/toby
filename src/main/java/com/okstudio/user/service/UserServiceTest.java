package com.okstudio.user.service;

import static com.okstudio.user.service.UserService.MIN_LOGCOUNT_FOL_SILVER;
import static com.okstudio.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.okstudio.user.dao.UserDao;
import com.okstudio.user.domain.Level;
import com.okstudio.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {	
	@Autowired
	private UserDao userDao;
	@Autowired
	UserService userService;
	@Autowired
	PlatformTransactionManager transactionManager;
	
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
			new User("001_bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOL_SILVER-1, 0),
			new User("002_joytouch", "강명", "p2", Level.BASIC, MIN_LOGCOUNT_FOL_SILVER, 0),
			new User("003_erwins", "신승환", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
			new User("004_madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
			new User("005_green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)			
		);		
	}
	
	@Test
	public void add() {
		this.userDao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithLevel.setLevel(null);
		
		this.userService.add(userWithLevel);
		this.userService.add(userWithoutLevel);
		
		User userWithLevelRead = this.userDao.get(userWithLevel.getId());
		User userWithouttLevelRead = this.userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithouttLevelRead.getLevel(), is(userWithoutLevel.getLevel()));
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		this.userDao.deleteAll();
		for(User user : users) {
			userDao.add(user);
		}
		
		this.userService.upgradeLevels();
		
		this.checkLevel(users.get(0), false);
		this.checkLevel(users.get(1), true);
		this.checkLevel(users.get(2), false);
		this.checkLevel(users.get(3), true);
		this.checkLevel(users.get(4), false);
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		testUserService.setTransactionManager(this.transactionManager);
		
		userDao.deleteAll();
		for(User user : users) {
			userDao.add(user);
		}
		
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch(TestUserServiceException e) {}
		
		this.checkLevel(users.get(1), false);
	}
	
	private void checkLevel(User user, boolean upgraded){
		User userUpdate = userDao.get(user.getId());
		if(upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
		
	}
	
	static class TestUserService extends UserService {
		private String id;
		
		private TestUserService(String id) {
			this.id = id;
		}
		
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) {
				throw new TestUserServiceException();
			}
			super.upgradeLevel(user);
		}		
	}
	
	static class TestUserServiceException extends RuntimeException {}
	
//	@Test
//	public void bean() {
//		assertThat(this.userService, is(notNullValue()));
//	}
}


