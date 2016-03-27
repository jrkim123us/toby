package com.okstudio.user.dao;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.okstudio.user.SqlMapConfig;

public class UserSqlMapConfig implements SqlMapConfig {

	@Override
	public Resource getSqlMapResource() {
		return new ClassPathResource("sqlmap.xml", UserDao.class);
	}

}
