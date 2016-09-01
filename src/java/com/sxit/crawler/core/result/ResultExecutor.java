package com.sxit.crawler.core.result;

import java.lang.ref.WeakReference;
import java.util.concurrent.BlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sxit.crawler.core.fetch.FetchEntry;
import com.sxit.crawler.core.fetch.FetchResultEntry;

public abstract class ResultExecutor implements Runnable{
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 抓取结果队列
	 */
	protected BlockingQueue<FetchEntry> resultQueue;

	/**
	 * 待抓取实体（URL）
	 */
	protected FetchEntry fetchEntry;
	
	public ResultExecutor(BlockingQueue<FetchEntry> resultQueue,
			FetchEntry fetchEntry) {
		this.resultQueue = resultQueue;
		this.fetchEntry = fetchEntry;
	}

	public abstract void processResult();
	
	/**
	 * 校验必要条件 
	 * @return 通过返回true， 不通过返回false
	 */
	protected boolean verifyRequired() {
		if (null == fetchEntry.getResult()) {
			return false;
		}
		StringBuffer pageContent = fetchEntry.getResult().getPageContent();
		if (null == pageContent || pageContent.length() <= 0) {
			return false;
		}
		return true;
	}
	
	
	protected Document parseHtmlContent(FetchEntry entry) {
		Document doc = null;
		try {
			doc = Jsoup.parse(entry.getResult().getPageContent().toString());
		} catch (Exception e) {
			log.warn("HTML Parse Error. URL:{}, Error:{}", entry.getUrl(), e.getMessage());
			if (log.isDebugEnabled()) {
				log.error(e.getMessage(), e);
			}
		}
		return doc;
	}
	
	/**
	 * 释放FetchEntity资源
	 */
	private void freeFetchEntityResource() {
		FetchResultEntry result = fetchEntry.getResult();
		if (null != result) {
			if (null != result.getPageContent()) {
				result.getPageContent().delete(0, result.getPageContent().length());
			}
			result.setPageContent(null);
			new WeakReference<FetchResultEntry>(result);
			result = null;
		}
		new WeakReference<FetchEntry>(fetchEntry);
		fetchEntry = null;
	}
	

	public void run() {
		processResult();
		freeFetchEntityResource();//释放内存资源
	}

}
