package com.okstudio.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.okstudio.user.dao.UserDao;
import com.okstudio.user.domain.Level;
import com.okstudio.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	@Autowired
	ApplicationContext context;
	@Autowired
	private UserDao userDao;
	@Autowired
	UserService userService;
	
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
			new User("001_bumjin", "박범진", "p1", Level.BASIC, 49, 0),
			new User("002_joytouch", "강명", "p2", Level.BASIC, 50, 0),
			new User("003_erwins", "신승환", "p3", Level.SILVER, 60, 29),
			new User("004_madnite1", "이상호", "p4", Level.SILVER, 60, 30),
			new User("005_green", "오민규", "p5", Level.GOLD, 100, 100)			
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
	public void upgradeLevels(){
		this.userDao.deleteAll();
		for(User user : users) {
			userDao.add(user);
		}
		
		this.userService.upgradeLevels();
		
		this.checkLevel(users.get(0), Level.BASIC);
		this.checkLevel(users.get(1), Level.SILVER);
		this.checkLevel(users.get(2), Level.SILVER);
		this.checkLevel(users.get(3), Level.GOLD);
		this.checkLevel(users.get(4), Level.GOLD);
	}
	
	private void checkLevel(User user, Level expectedLevel){
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLevel(), is(expectedLevel));
	}
	
//	@Test
//	public void bean() {
//		assertThat(this.userService, is(notNullValue()));
//	}
}
