package com.sxit.crawler;

import org.springframework.beans.BeanUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sxit.crawler.core.CrawlConfig;
import com.sxit.crawler.module.CrawlModule;

/**
 * 系统参数定义：
 * 
 * module.home：模块所在的路径
 * app.home:爬虫应用程序所在目录
 * @author Administrator
 *
 */
public class Startup {
	
	private CrawlConfig crawlConfig;
	
	private static void printUsage() {
		System.out.println("Startup #moduleClassName# #jobName# #appId#");
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("\t#moduleClassName#为抓取模块的类全名");
		System.out.println("\t#jobName#为抓取任务名称");
		System.out.println("\t#appId#为对应的音乐应用ID，来自于tbas_musicapp表");
		System.out.println("示例：");
		System.out.println("java Startup com.sxit.crawler.module.singerrepos.kuwo.KuWoCrawlModule 酷我音乐库爬虫 1");
		System.out.println("------------------------------------------------------------------------------------");
	}

	public static void main(String[] args) {
		if (null == args || args.length < 3) {
			printUsage();
			System.exit(0);
		}
		String moduleClassName = null;
		String jobName = null;
		long appId = -1;
		try {
			moduleClassName = args[0];
			jobName = args[1];
			appId = Long.parseLong(args[2]);
			System.out.printf("moduleClassName:%s\r\n", moduleClassName);
			System.out.printf("jobName:%s\r\n", jobName);
			System.out.printf("appId:%s\r\n", appId);
		} catch (Exception e) {
			e.printStackTrace();
			printUsage();
			System.exit(0);
		}
		
		
		Startup startup = new Startup();
		startup.initConfig(jobName, appId);
		
		startup.initContext();
		startup.startModule(moduleClassName);
	}
	
	public void initConfig(String jobName, long appId) {
		crawlConfig = new CrawlConfig();
		crawlConfig.setCrawlJobName(jobName);
		crawlConfig.setAppId(appId);
	}
	
	public void initContext(){
		new ClassPathXmlApplicationContext("classpath*:*-beans.xml");
	}
	
	public void startModule(String moduleClassName) {
		CrawlModule crawlModule = null;
		try {
			crawlModule = (CrawlModule)BeanUtils.instantiateClass(Class.forName(moduleClassName));
			crawlModule.setCrawlConfig(crawlConfig);
		} catch (Exception e) {
			throw new Error("抓取模块初始化错误", e);
		}
		crawlModule.execute();
	}
}
