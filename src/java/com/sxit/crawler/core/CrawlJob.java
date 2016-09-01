package com.sxit.crawler.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sxit.crawler.commons.exception.SystemException;
import com.sxit.crawler.core.fetch.FetchController;
import com.sxit.crawler.core.fetch.FetchEntityBuilder;
import com.sxit.crawler.core.fetch.FetchEntry;
import com.sxit.crawler.core.fetch.FetchExecutorBuilder;
import com.sxit.crawler.core.fetch.UserAgentProvider;
import com.sxit.crawler.core.result.ResultController;
import com.sxit.crawler.core.result.ResultExecutorBuilder;

public class CrawlJob {
	
	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 待抓取队列
	 */
	private BlockingQueue<FetchEntry> fetchQueue;
	
	/**
	 * 抓取结果队列
	 */
	private BlockingQueue<FetchEntry> resultQueue;
	
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
	 * 抓取控制器
	 */
	private FetchController fetchController;
	
	/**
	 * 结果处理控制器
	 */
	private ResultController resultController;
	
	/**
	 * FetchExecutor构造器实例
	 */
	private FetchExecutorBuilder fetchExecutorBuilder;
	
	/**
	 * ResultExecutor构造器实例
	 */
	private ResultExecutorBuilder resultExecutorBuilder;
	
	
	private ThreadGroup threadGroup;
	
	private String jobName;
	
	private boolean hashSumitEntEntity = false;//用于标识是否提交结束标识（FetchEntry.END_ENTRY对象）

	/**
	 * 
	 * @param jobName 任务名称
	 * @param fetchQueueLength 抓取队列长度
	 * @param resultQueueLength 抓取结果处理队列长度
	 * @param fetchThreadPoolSize 抓取线程池大小
	 * @param resultThreadPoolSize 抓取结果处理线程池大小
	 * @param fetchExecutorBuilder 抓取Executor构造器
	 * @param resultExecutorBuilder 抓取结果Executor构造器
	 */
	public CrawlJob(CrawlConfig config) {
		this.jobName = config.getCrawlJobName();
		this.fetchQueueLength = config.getFetchQueueLength();
		this.resultQueueLength = config.getResultQueueLength();
		this.fetchThreadPoolSize = config.getFetchThreadPoolSize();
		this.resultThreadPoolSize = config.getResultThreadPoolSize();
		this.fetchExecutorBuilder = config.getFetchExecutorBuilder();
		this.resultExecutorBuilder = config.getResultExecutorBuilder();
		init();
	}
	
	private void init() {
		fetchQueue = new LinkedBlockingQueue<FetchEntry>(this.fetchQueueLength);
		resultQueue = new LinkedBlockingQueue<FetchEntry>(this.resultQueueLength);
		threadGroup = new ThreadGroup(jobName+"_thread_group");
		
		//初始化抓取控制器
		fetchController = new FetchController(
				this.threadGroup, 
				this.jobName+"_fetch_controller", 
				this.fetchQueue, 
				this.resultQueue, 
				this.fetchThreadPoolSize, 
				this.fetchExecutorBuilder
				);
		
		//初始化抓取结果处理控制器
		resultController = new ResultController(
				this.threadGroup, 
				this.jobName+"_result_controller", 
				this.resultQueue, 
				this.resultThreadPoolSize, 
				this.resultExecutorBuilder
				);
	}
	
	/**
	 * 启动JOB
	 */
	public void startJob() {
		log.info("正在启动采集控制器... ... ");
		fetchController.start();
		resultController.start();
	}
	
	/**
	 * 等待JOB退出
	 */
	public void waitJobExit() {
		if (!hashSumitEntEntity) {
			complete();
		}
		while (fetchController.isAlive() || resultController.isAlive()) {
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				//
			}
		}
		log.info("采集控制器正常退出");
	}
	
	/**
	 * 依据URL方式提交，使用系统默认的UserAgent
	 * @param url
	 */
	public void submitUrl(String url) {
		FetchEntry fetchEntry = FetchEntityBuilder.buildFetchEntry(url);
		submit(fetchEntry);
	}
	
	/**
	 * 依据URL方式提交，使用自定义的UserAgent
	 * @param url
	 * @param userAgentProvider
	 */
	public void submitUrl(String url, UserAgentProvider userAgentProvider) {
		FetchEntry fetchEntry = FetchEntityBuilder.buildFetchEntry(url, userAgentProvider);
		submit(fetchEntry);
	}
	
	/**
	 * 直接提交FetchEntry
	 * @param entry
	 */
	public void submit(FetchEntry entry) {
		try {
			log.info("提交URL:{}, DETAUL:{}", entry.getUrl(), entry);
			fetchQueue.put(entry);
		} catch (InterruptedException e) {
			//
		} catch (Exception e) {
			log.error("提交URL错误:{}", e.getMessage());
			throw new SystemException(e);
		}
	}
	
	/**
	 * 标识CarwlJob需要采集的URL发送完成，采集控制器进入关闭流程
	 */
	public void complete() {
		try {
			fetchQueue.put(FetchEntry.END_ENTRY);
			hashSumitEntEntity = true;
		} catch (Exception e) {
			//TODO END_ENTRY写入失败的一个补全措施
		}
	}
	
}
