package com.okstudio.user.sqlservice.updatable;

public class SqlUpdateFailureException extends RuntimeException {
	public SqlUpdateFailureException(String message) {
		super(message);
	}
	
	public SqlUpdateFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}
