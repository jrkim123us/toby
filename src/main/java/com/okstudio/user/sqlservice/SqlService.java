package com.okstudio.user.sqlservice;

public interface SqlService {
	String getSql(String key) throws SqlRetrieveFailureException;
}
