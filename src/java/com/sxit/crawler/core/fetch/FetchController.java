package com.sxit.crawler.core.fetch;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sxit.crawler.commons.BasicThread;

/**
 * 采集任务分发与控制器
 * 1、从抓取队列中获取抓取实体，分发给具体的采集线程执行采集
 * 2、如果从抓取实体中获取到FetchEntry.END_ENTRY则表示队列已经执行完成，Job将执行关闭流程
 * @author Administrator
 *
 */
public class FetchController extends BasicThread{

	/**
	 * 待抓取队列
	 */
	private BlockingQueue<FetchEntry> fetchQueue;
	
	/**
	 * 抓取结果队列
	 */
	private BlockingQueue<FetchEntry> resultQueue;
	
	
	/**
	 * 抓取线程组
	 */
	private ExecutorService executorService;
	
	/**
	 * FetchExecutor构造器实例
	 */
	private FetchExecutorBuilder fetchExecutorBuilder;
	
	/**
	 * 使用自定义抓取器
	 * @param threadGroup 线程组
	 * @param threadName 线程名称
	 * @param fetchQueue 抓取队列
	 * @param resultQueue 抓取结果处理队列
	 * @param threadPoolSize 线程池大小
	 * @param fetchExecutorClassName 自定义抓取器实现类全名
	 */
	public FetchController(ThreadGroup threadGroup, 
			String threadName, 
			BlockingQueue<FetchEntry> fetchQueue, 
			BlockingQueue<FetchEntry> resultQueue, 
			int threadPoolSize,
			FetchExecutorBuilder fetchExecutorBuilder) {
		super(threadGroup, threadName);
		this.fetchQueue = fetchQueue;
		this.resultQueue = resultQueue;
		this.executorService = Executors.newFixedThreadPool(threadPoolSize); 
		this.fetchExecutorBuilder = fetchExecutorBuilder;
	}


	@Override
	public void run() {
		isRunning = true;
		log.info("FetchController启动: {}.", getThreadGroup().getName()+":"+getName());
		while (isRunning) {
			try {
				//由JOB线程统一分发，可以降低设计难度。
				//（当遇到END_ENTRY对象之后可以直接全部退出，如果不是由JOB统一分发任务，则需要考虑线程间的通信机制）
				FetchEntry fetchEntry = fetchQueue.take();

				if (fetchEntry == FetchEntry.END_ENTRY) {
					//如果遇到尾部标识，则完成JOB处理过程，等待线程池完成后直接退出。
					executorService.shutdown();//调用 shutdown 拒绝传入任务
					while (!executorService.isTerminated()) {//等待所有任务执行完成
						mysleep(1000);
						//TODO 在此加入JOB最大执行时长限制
					}
					isRunning = false;
					try {
						resultQueue.put(FetchEntry.END_ENTRY);//将抓取结果中放入结束标识
						if (log.isDebugEnabled()) {
							log.info("写入结束标识至结果队列");
						}
					} catch (Exception e) {
						if (log.isDebugEnabled()) {
							log.error(e.getMessage(), e);
						}
						//TODO END_ENTRY写入失败的一个补全措施
					}
				} else {
					//将当前抓取对象放入线程中抓取
					if (log.isDebugEnabled()) {
						log.info("开始执行采集动作->{}", fetchEntry);
					}
					executorService.execute(fetchExecutorBuilder.buildFetchExecutor(resultQueue, fetchEntry));
				}
			} catch (InterruptedException e) {
				log.warn("FetchController执行过程产生中断信号: {}.", getThreadGroup().getName()+":"+getName());
				if (log.isDebugEnabled()) {
					log.warn("FetchController执行过程产生中断信号: {}.", getThreadGroup().getName()+":"+getName());
					log.error(e.getMessage(), e);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		log.info("FetchController退出: {}.", getThreadGroup().getName()+":"+getName());
	}
	

}
