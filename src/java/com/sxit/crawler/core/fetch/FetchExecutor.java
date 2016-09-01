package com.sxit.crawler.core.fetch;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 抓取器抽象类，为自定义抓取器提供扩展
 * 所有抓取器基本的逻辑应当包含如下两部：
 * 1、通过Job分派抓取实体，执行网页内容下载动作
 * 2、下载完成之后，将抓取结果放入resultQueue队列
 * @author Administrator
 *
 */
public abstract class FetchExecutor implements Runnable {
	
	

	protected Logger log = LoggerFactory.getLogger(getClass());
	
	protected FetchExecutorConfig fetchExecutorConfig;
	
	/**
	 * 待抓取实体（URL）
	 */
	protected FetchEntry fetchEntry;
	
	/**
	 * 抓取结果队列
	 */
	protected BlockingQueue<FetchEntry> resultQueue;
	
	public FetchExecutor(FetchExecutorConfig fetchExecutorConfig, BlockingQueue<FetchEntry> resultQueue,
			FetchEntry fetchEntry) {
		this.fetchExecutorConfig = fetchExecutorConfig;
		this.fetchEntry = fetchEntry;
		this.resultQueue = resultQueue;
	}

	//@Override
	public void run() {
		process();
	}
	
	public abstract void process();

	public FetchExecutorConfig getFetchExecutorConfig() {
		return fetchExecutorConfig;
	}

	public void setFetchExecutorConfig(FetchExecutorConfig fetchExecutorConfig) {
		this.fetchExecutorConfig = fetchExecutorConfig;
	}
	
}
