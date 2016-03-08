package com.okstudio.user.service;

import com.okstudio.user.domain.User;

public interface UserService {
	void add(User user);
	void upgradeLevels();
	
}
