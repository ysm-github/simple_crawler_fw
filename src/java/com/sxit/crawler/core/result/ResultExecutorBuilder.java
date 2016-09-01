package com.sxit.crawler.core.result;

import java.util.concurrent.BlockingQueue;

import com.sxit.crawler.core.fetch.FetchEntry;

/**
 * ResultExecutor构造器接口
 * @author Administrator
 *
 */
public interface ResultExecutorBuilder {

	/**
	 * ResultExecutor构造器，
	 * 主要是将构造ResultExecutor实例的控制权交个上层业务逻辑，方便回调处理。
	 * @param resultQueue 返回结果队列实例
	 * @param fetchEntry 抓取对象（已经抓取内容之后的）
	 * @return
	 */
	ResultExecutor buildResultExecutor(BlockingQueue<FetchEntry> resultQueue, FetchEntry fetchEntry);
//	private ResultExecutor buildResultExecutor(FetchEntry fetchEntry) {
//		if (StringUtils.isBlank(resultExecutorClassName)) {
//			resultExecutorClassName = DEFAULT_RESULT_EXECUTOR_CLASS;
//		}
//		try {
//			Class<?> resultExecutorClass = Class.forName(resultExecutorClassName);
//			ResultExecutor resultExecutor = (ResultExecutor)resultExecutorClass.newInstance();
//			resultExecutor.setFetchEntry(fetchEntry);
//			resultExecutor.setResultQueue(resultQueue);
//			return resultExecutor;
//		} catch (ClassNotFoundException e) {
//			throw new Error("找不到抓取结果处理器实现类", e);
//		} catch (Exception e) {
//			throw new Error("抓取结果处理器实例化错误", e);
//		}
//	}
	
	
}
