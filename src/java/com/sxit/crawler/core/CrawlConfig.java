package com.sxit.crawler.core;

import com.sxit.crawler.core.fetch.FetchExecutorBuilder;
import com.sxit.crawler.core.result.ResultExecutorBuilder;

public class CrawlConfig {
	
	
	/**
	 * 对应的应用ID编号
	 */
	private long appId;

	/**
	 * 抓取名称
	 */
	private String crawlJobName;

	/**
	 * 抓取队列长度
	 */
	private int fetchQueueLength;
	
	/**
	 * 结果处理队列长度
	 */
	private int resultQueueLength;
	
	/**
	 * 抓取线程池大小
	 */
	private int fetchThreadPoolSize;
	
	/**
	 * 结果处理线程池大小
	 */
	private int resultThreadPoolSize;
	
	/**
	 * FetchExecutor构造器实例
	 */
	private FetchExecutorBuilder fetchExecutorBuilder;
	
	/**
	 * ResultExecutor构造器实例
	 */
	private ResultExecutorBuilder resultExecutorBuilder;
	
	/**
	 * 将所有的队列，线程池设置为1
	 */
	public void setAllToOne() {
		setFetchQueueLength(1);
		setFetchThreadPoolSize(1);
		setResultThreadPoolSize(1);
		setResultQueueLength(1);
	}
	
	
	public int getFetchQueueLength() {
		return fetchQueueLength;
	}
	public void setFetchQueueLength(int fetchQueueLength) {
		this.fetchQueueLength = fetchQueueLength;
	}
	public int getResultQueueLength() {
		return resultQueueLength;
	}
	public void setResultQueueLength(int resultQueueLength) {
		this.resultQueueLength = resultQueueLength;
	}
	public int getFetchThreadPoolSize() {
		return fetchThreadPoolSize;
	}
	public void setFetchThreadPoolSize(int fetchThreadPoolSize) {
		this.fetchThreadPoolSize = fetchThreadPoolSize;
	}
	public int getResultThreadPoolSize() {
		return resultThreadPoolSize;
	}
	public void setResultThreadPoolSize(int resultThreadPoolSize) {
		this.resultThreadPoolSize = resultThreadPoolSize;
	}
	public String getCrawlJobName() {
		return crawlJobName;
	}
	public void setCrawlJobName(String crawlJobName) {
		this.crawlJobName = crawlJobName;
	}
	public FetchExecutorBuilder getFetchExecutorBuilder() {
		return fetchExecutorBuilder;
	}
	public void setFetchExecutorBuilder(FetchExecutorBuilder fetchExecutorBuilder) {
		this.fetchExecutorBuilder = fetchExecutorBuilder;
	}
	public ResultExecutorBuilder getResultExecutorBuilder() {
		return resultExecutorBuilder;
	}
	public void setResultExecutorBuilder(ResultExecutorBuilder resultExecutorBuilder) {
		this.resultExecutorBuilder = resultExecutorBuilder;
	}
	public long getAppId() {
		return appId;
	}
	public void setAppId(long appId) {
		this.appId = appId;
	}
}
