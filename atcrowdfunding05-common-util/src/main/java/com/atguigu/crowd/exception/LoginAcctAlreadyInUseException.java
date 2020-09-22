package com.atguigu.crowd.exception;

/**
 * This exception is thrown repeatedly if the login account is detected when saving or updating Admin 
 * @author Lenovo
 *
 */
public class LoginAcctAlreadyInUseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LoginAcctAlreadyInUseException() {
		super();
	}

	public LoginAcctAlreadyInUseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LoginAcctAlreadyInUseException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginAcctAlreadyInUseException(String message) {
		super(message);
	}

	public LoginAcctAlreadyInUseException(Throwable cause) {
		super(cause);
	}

}
