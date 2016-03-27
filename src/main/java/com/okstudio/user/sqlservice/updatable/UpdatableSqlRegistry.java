package com.okstudio.user.sqlservice.updatable;

import java.util.Map;

import com.okstudio.user.sqlservice.SqlRegistry;

public interface UpdatableSqlRegistry extends SqlRegistry {
	public void updateSql(String key, String sql) throws SqlUpdateFailureException;
	
	public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;
}
