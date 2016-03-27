package com.okstudio.user.sqlservice;

public class SqlRetrieveFailureException extends RuntimeException {
	public SqlRetrieveFailureException(String message) {
		super(message);
	}
	
	public SqlRetrieveFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}
