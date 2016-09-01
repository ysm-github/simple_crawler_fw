package com.sxit.crawler.commons;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		SpringContextHolder.applicationContext = ctx;
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
