package com.okstudio.user.service;

import java.util.List;

import com.okstudio.user.domain.User;

public interface UserService {
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	void update(User user);
	
	void upgradeLevels();	
}
