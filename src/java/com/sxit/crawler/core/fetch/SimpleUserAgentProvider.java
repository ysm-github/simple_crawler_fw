package com.sxit.crawler.core.fetch;


public class SimpleUserAgentProvider implements UserAgentProvider{

	
	public SimpleUserAgentProvider(String from, String userAgent) {
		super();
		this.from = from;
		this.userAgent = userAgent;
	}

	private String userAgent;
	
	private String from;
	public String getUserAgent() {
		return this.userAgent;
	}

	public String getFrom() {
		return this.from;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String toString() {
		return "SimpleUserAgentProvider [userAgent=" + userAgent + ", from="
				+ from + "]";
	}
	
	

}
