package com.okstudio.user.sqlservice;

import java.util.Map;

public class SimpleSqlService implements SqlService {
	private Map<String, String> sqlMap;
	
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
	
	@Override
	public String getSql(String key) throws SqlRetrieveFailureException {
		String sql = sqlMap.get(key);
		
		if(sql == null) {
			throw new SqlRetrieveFailureException(key + "에 대한 SQL을 찾을 수 없습니다.");
		} else {
			return sql;			
		}
	}

}
