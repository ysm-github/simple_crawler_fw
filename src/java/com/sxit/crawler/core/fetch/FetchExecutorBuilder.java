package com.sxit.crawler.core.fetch;

import java.util.concurrent.BlockingQueue;


/**
 * FetchExecutor构造器
 * @author Administrator
 *
 */
public interface FetchExecutorBuilder {

	/**
	 * 用于构造FetchExecutor实例
	 * @param resultQueue 返回结果队列实例
	 * @param fetchEntry 抓取对象（还没有抓取的）
	 * @return
	 */
	FetchExecutor buildFetchExecutor(BlockingQueue<FetchEntry> resultQueue, FetchEntry fetchEntry);
}
