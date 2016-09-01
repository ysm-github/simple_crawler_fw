package com.sxit.crawler.module;

import java.util.Map;

import com.sxit.crawler.core.CrawlConfig;
import com.sxit.crawler.core.fetch.FetchExecutorBuilder;
import com.sxit.crawler.core.fetch.UserAgentProvider;
import com.sxit.crawler.core.result.ResultExecutorBuilder;

/**
 * 抓取器，实现具体的页面抓取逻辑，一个采集模块将由多个抓取器构成，
 * 每个抓取器负责一个具体采集阶段的网页抓取、与清洗
 * @author Administrator
 *
 */
public abstract class CrawlProcess {
	
	public CrawlProcess(UserAgentProvider userAgentProvider,
			CrawlModule crawlModule, CrawlConfig crawlConfig) {
		this.userAgentProvider = userAgentProvider;
		this.crawlModule = crawlModule;
		this.crawlConfig = crawlConfig;
	}


	/**
	 * 抓取任务对应的useragent
	 */
	protected final UserAgentProvider userAgentProvider;
	
	/**
	 * 改抓取任务所归属的采集模块
	 */
	protected final CrawlModule crawlModule;
	
	protected final CrawlConfig crawlConfig;
	
	public CrawlConfig getCrawlConfig() {
		return crawlConfig;
	}


	/**
	 * 默认抓取构造器，用于提供特地的抓取器
	 */
	protected FetchExecutorBuilder fetchExecutorBuilder;
	
	/**
	 * 默认结果处理构造器
	 */
	protected ResultExecutorBuilder resultExecutorBuilder;
	
	/**
	 * 处理入口
	 */
	public abstract void process(Map<String, Object> param);
	
	
	public UserAgentProvider getUserAgentProvider() {
		return userAgentProvider;
	}

	public CrawlModule getCrawlModule() {
		return crawlModule;
	}
}
