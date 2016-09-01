package com.sxit.crawler.commons.exception;

public class ReadTimeOutException extends RuntimeException {

	static final long serialVersionUID = -8053228381883858388L;

	public ReadTimeOutException() {
		super();
	}

	public ReadTimeOutException(String message) {
		super(message);
	}

	public ReadTimeOutException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReadTimeOutException(Throwable cause) {
		super(cause);
	}
}
