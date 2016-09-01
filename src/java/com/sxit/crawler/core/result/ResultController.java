package com.sxit.crawler.core.result;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sxit.crawler.commons.BasicThread;
import com.sxit.crawler.core.fetch.FetchEntry;

/**
 * 抓取结果处理控制器
 * @author Administrator
 *
 */
public class ResultController extends BasicThread {
	
	/**
	 * 抓取结果队列
	 */
	private BlockingQueue<FetchEntry> resultQueue;
	
	
	/**
	 * 结果处理线程组
	 */
	private ExecutorService executorService;
	
	/**
	 * ResultExecutor构造器实例，是在上层实例化
	 */
	private ResultExecutorBuilder resultExecutorBuilder;
	
	
	/**
	 * 使用自定义抓取器
	 * @param threadGroup 线程组
	 * @param threadName 线程名称
	 * @param resultQueue 抓取结果处理队列
	 * @param threadPoolSize 线程池大小
	 * @param fetchExecutorClassName 自定义抓取结果处理器实现类全名
	 */
	public ResultController(ThreadGroup threadGroup, 
			String threadName, 
			BlockingQueue<FetchEntry> resultQueue, 
			int threadPoolSize,
			ResultExecutorBuilder resultExecutorBuilder) {
		super(threadGroup, threadName);
		this.resultQueue = resultQueue;
		this.executorService = Executors.newFixedThreadPool(threadPoolSize); 
		this.resultExecutorBuilder = resultExecutorBuilder;
	}

	@Override
	public void run() {
		isRunning = true;
		log.info("ResultController启动: {}.", getThreadGroup().getName()+":"+getName());
		while (isRunning) {
			try {
				//由JOB线程统一分发，可以降低设计难度。
				//（当遇到END_ENTRY对象之后可以直接全部退出，如果不是由JOB统一分发任务，则需要考虑线程间的通信机制）
				FetchEntry fetchEntry = resultQueue.take();

				if (fetchEntry == FetchEntry.END_ENTRY) {
					//如果遇到尾部标识，则完成JOB处理过程，等待线程池完成后直接退出。
					executorService.shutdown();//调用 shutdown 拒绝传入任务
					while (!executorService.isTerminated()) {//等待所有任务执行完成
						mysleep(1000);
						//TODO 在此加入JOB最大执行时长限制
					}
					isRunning = false;
				} else {
					//将当前抓取对象放入线程中抓取
					executorService.execute(resultExecutorBuilder.buildResultExecutor(resultQueue, fetchEntry));
				}
			} catch (InterruptedException e) {
				log.warn("ResultController执行过程产生中断信号: {}.", getThreadGroup().getName()+":"+getName());
				if (log.isDebugEnabled()) {
					log.warn("ResultController执行过程产生中断信号: {}.", getThreadGroup().getName()+":"+getName());
					log.error(e.getMessage(), e);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				// TODO: handle exception
			}
		}
		log.info("ResultController退出: {}.", getThreadGroup().getName()+":"+getName());
	}
}
