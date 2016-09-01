package com.sxit.crawler.commons.exception;

public class ResourceInitException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8443830351443302738L;

	public ResourceInitException() {
		super();
	}

	public ResourceInitException(ExceptionType type, String message) {
		this("["+type+"]-->"+message);
	}
	
	public ResourceInitException(ExceptionType type, String message, Throwable cause) {
		this("["+type+"]-->"+message, cause);
	}
	
	public ResourceInitException(String message) {
		super(message);
	}

	public ResourceInitException(ExceptionType type, Throwable cause) {
		this("["+type+"]", cause);
	}
	
	public ResourceInitException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceInitException(Throwable cause) {
		super(cause);
	}
	
	public static enum ExceptionType {
		DATA_DIR_CREATE_ERROR
	}
}
