package com.sxit.crawler.commons.exception;


/**
 * @description: httpclient连接超时
 */
public class ConnectTimeOutException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ConnectTimeOutException() {
		super();
	}

	public ConnectTimeOutException(String message) {
		super(message);
	}

	public ConnectTimeOutException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConnectTimeOutException(Throwable cause) {
		super(cause);
	}


}
