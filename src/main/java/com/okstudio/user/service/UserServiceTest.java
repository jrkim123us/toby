package com.okstudio.user.service;

import static com.okstudio.user.service.UserServiceImpl.MIN_LOGCOUNT_FOL_SILVER;
import static com.okstudio.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.okstudio.user.AppContext;
import com.okstudio.user.TestAppContext;
import com.okstudio.user.dao.UserDao;
import com.okstudio.user.domain.Level;
import com.okstudio.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppContext.class, TestAppContext.class})
public class UserServiceTest {	
	@Autowired private UserDao userDao;
	@Autowired UserService userService;
	@Autowired UserService testUserService;
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired MailSender mailSender;
	@Autowired ApplicationContext context;	
	
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
			new User("001_bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOL_SILVER-1, 0, "bumjin@mail.com"),
			new User("002_joytouch", "강명", "p2", Level.BASIC, MIN_LOGCOUNT_FOL_SILVER, 0, "joytouch@mail.com"),
			new User("003_erwins", "신승환", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1, "erwins@mail.com"),
			new User("004_madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "madnite1@mail.com"),
			new User("005_green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "green@mail.com")			
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
	@DirtiesContext
	public void upgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);		
		
		userServiceImpl.setUserDao(mockUserDao);
		
		MailSender mockMailSender = mock(MailSender.class);		
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getLevel(), is(Level.SILVER));
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLevel(), is(Level.GOLD));
		
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
	}
	
	@Test
	public void advisorAutoProxyCreator() {
		assertThat(this.testUserService, instanceOf(java.lang.reflect.Proxy.class));
	}
	
	@Test
	@DirtiesContext
	public void upgradeAllOrNothing() throws Exception {
		userDao.deleteAll();
		for(User user : users) {
			userDao.add(user);
		}
		
		try {
			this.testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch(TestUserServiceException e) {}
		
		this.checkLevel(users.get(1), false);
	}
	
	@Test
//	@Transactional(readOnly=true)
	@Rollback(false)
	public void transactionSync() {		
		this.userService.deleteAll();
		
		this.userService.add(this.users.get(0));
		this.userService.add(this.users.get(1));
	}

	
//	@Test(expected=TransientDataAccessResourceException.class)
//	public void readOnlyTransactionAttribute() {		
//		this.testUserService.getAll();
//	}
	
	private void checkLevel(User user, boolean upgraded){
		User userUpdate = userDao.get(user.getId());
		if(upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
		
	}
	static class MockUserDao implements UserDao {
		private List<User> users;
		private List<User> updated = new ArrayList();
		
		private MockUserDao(List<User> users) {
			this.users = users;
		}
		
		public List<User> getUpdated() {
			return this.updated;
		}
		
		public List<User> getAll() {
			return this.users;
		}
		
		public void update(User user) {
			updated.add(user);
		}
		
		public void add(User user) {throw new UnsupportedOperationException();}
		public void deleteAll() {throw new UnsupportedOperationException();}
		public User get(String id) {throw new UnsupportedOperationException();}
		public int getCount() {throw new UnsupportedOperationException();}
	}
	
	static class MockMailSender implements MailSender {
		private List<String> requests = new ArrayList<String>();
		
		public List<String> getRequests() {
			return this.requests;
		}

		@Override
		public void send(SimpleMailMessage mailMessage) throws MailException {
			this.requests.add(mailMessage.getTo()[0]);			
		}

		@Override
		public void send(SimpleMailMessage... mailMessages) throws MailException {			
		}
		
	}

	
	public static class TestUserServiceImpl extends UserServiceImpl {
		private String id = "004_madnite1";

		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) {
				throw new TestUserServiceException();
			}
			super.upgradeLevel(user);
		}		
		public List<User> getAll() {			
			for(User user : super.getAll()) {				
				super.update(user);
			}
			return null;
		}		
	}
	
	static class TestUserServiceException extends RuntimeException {}
	
//	@Test
//	public void bean() {
//		assertThat(this.userService, is(notNullValue()));
//	}
}


