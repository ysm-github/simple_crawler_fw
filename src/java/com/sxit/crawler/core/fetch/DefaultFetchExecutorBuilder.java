package com.sxit.crawler.core.fetch;

import java.util.concurrent.BlockingQueue;

public class DefaultFetchExecutorBuilder implements FetchExecutorBuilder{
	private final FetchExecutorConfig fetchExecutorConfig;
	
	public DefaultFetchExecutorBuilder() {
		this(FetchExecutorConfig.FETCH_EXECUTOR_CONFIG);
	}

	public DefaultFetchExecutorBuilder(FetchExecutorConfig fetchExecutorConfig) {
		this.fetchExecutorConfig = fetchExecutorConfig;
	}

	//@Override
	public FetchExecutor buildFetchExecutor(
			BlockingQueue<FetchEntry> resultQueue, FetchEntry fetchEntry) {
		return new DefaultFetchExecutor(fetchExecutorConfig, resultQueue, fetchEntry);
	}

	public FetchExecutorConfig getFetchExecutorConfig() {
		return fetchExecutorConfig;
	}
	
}
