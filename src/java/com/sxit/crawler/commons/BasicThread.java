package com.sxit.crawler.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasicThread extends Thread{

	protected final int STOP_TIMEOUT = 5*1000;//等待超时时间
	
	protected volatile boolean isRunning = false;
	
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	public BasicThread(ThreadGroup threadGroup, String string) {
		super(threadGroup, string);
	}
	public BasicThread(String string) {
		super(string);
	}

	public void stopThread() {
		if (isRunning) {
			log.info("停止线程执行  {}-->{}.", getThreadGroup().getName()+":"+getName()+":"+getId());
			isRunning = false;
			interrupt();
		}
		long start = System.currentTimeMillis();
		while (isAlive()) {
			interrupt();
			if (System.currentTimeMillis()-start >= STOP_TIMEOUT) {
				throw new RuntimeException("线程停止超过等待时间["+STOP_TIMEOUT+"ss], ID="+getId()+" NAME="+getName());
			}
			mysleep(100);
		}
	}
	
	/**
	 * 睡眠i毫秒
	 * @param i
	 */
	public void mysleep(long i) {
		try {
			sleep(i);
		} catch (InterruptedException e) {
		}
	}
	
	public boolean getRunning() {
		return isRunning;
	}
}
