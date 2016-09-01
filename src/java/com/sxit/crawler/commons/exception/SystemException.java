package com.sxit.crawler.commons.exception;

public class SystemException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6886986897849907646L;

	public SystemException() {
		super();
	}

	public SystemException(String message) {
		super(message);
	}

	public SystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemException(Throwable cause) {
		super(cause);
	}
}
