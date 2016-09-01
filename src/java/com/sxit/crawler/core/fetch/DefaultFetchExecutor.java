package com.sxit.crawler.core.fetch;

import java.util.concurrent.BlockingQueue;


/**
 * 默认抓取器
 * 1、通过Job分派抓取实体，执行网页内容下载动作
 * 2、下载完成之后，将抓取结果放入resultQueue队列
 * @author Administrator
 *
 */
public class DefaultFetchExecutor extends FetchExecutor{
	

	public DefaultFetchExecutor(FetchExecutorConfig fetchExecutorConfig,
			BlockingQueue<FetchEntry> resultQueue, FetchEntry fetchEntry) {
		super(fetchExecutorConfig, resultQueue, fetchEntry);
	}

	@Override
	public void process() {
		if (fetch()) {//执行抓取页面动作
			try {
				resultQueue.put(fetchEntry);//将抓取结果写入结果队列
			} catch (InterruptedException e) {
				log.warn(e.getMessage(), e);
				//
			}
		}
	}
	
	private boolean fetch() {
		try {
			Fetch fetch = new FetchHTTP();
			long start = System.currentTimeMillis();
			fetchEntry = fetch.process(fetchEntry);
			long end = System.currentTimeMillis();
			log.info("耗时:{}，抓取URL:{}", (end-start), fetchEntry.getUrl());
			if (fetchExecutorConfig.getWaitTime() > 0) {
				Thread.sleep(fetchExecutorConfig.getWaitTime());
			}
			return true;
		} catch (Exception e) {
			log.warn("抓取错误，URL:{}，ErrorMessage:{}", fetchEntry.getUrl(), e.getMessage());
			if (log.isDebugEnabled()) {
				log.error("抓取错误:"+e.getMessage(), e);
			}
			return false;
		}
	}

}
