package com.sxit.crawler.commons;
import org.springframework.context.ApplicationContext;

public class BeanHelper {

	public static ApplicationContext getSpringContext() {
		return SpringContextHolder.getApplicationContext();
	}
	
	public static Object getBean(String name) {
		return getSpringContext().getBean(name);
	}
	
}