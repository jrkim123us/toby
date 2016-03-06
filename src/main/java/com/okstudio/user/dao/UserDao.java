package com.okstudio.user.dao;

import java.util.List;

import com.okstudio.user.domain.User;

public interface UserDao {
	public void add(User user);
	public User get(String id);
	public List<User> getAll();
	public void update(User user);
	public void deleteAll();
	public int getCount();	
}