package com.uantwerp.algorithms.exceptions;

public class CustomizedUncheckedException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4075676136165556904L;

	public CustomizedUncheckedException() {
		// TODO Auto-generated constructor stub
	}

	public CustomizedUncheckedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CustomizedUncheckedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public CustomizedUncheckedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CustomizedUncheckedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
