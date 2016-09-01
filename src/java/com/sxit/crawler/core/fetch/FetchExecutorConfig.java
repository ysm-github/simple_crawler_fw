package com.sxit.crawler.core.fetch;

public class FetchExecutorConfig {
	
	public final static FetchExecutorConfig FETCH_EXECUTOR_CONFIG = new FetchExecutorConfig();
	
	protected long waitTime = 0;//等待时间，毫秒，用于温柔抓取, 为小于或等于0表示不等待
	
	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

}
