package com.sxit.crawler.commons.exception;

public class PageProcessException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6886986897849907646L;

	/**
	 * 错误代码
	 */
	private int errorCode;

	public PageProcessException() {
		super();
	}

	public PageProcessException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	public PageProcessException(String message) {
		super(message);
	}

	public PageProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public PageProcessException(Throwable cause) {
		super(cause);
	}
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
