package com.sxit.crawler.module;

import java.io.File;
import java.util.Map;

import com.sxit.crawler.commons.SystemConstant;
import com.sxit.crawler.commons.jdbc.DatatableConfig;
import com.sxit.crawler.commons.jdbc.DatatableOperator;
import com.sxit.crawler.core.CrawlConfig;

/**
 * 抓取模块，通常将一个站点所对应的采集行为封装为一个采集模块，一个抓取模块由多个抓取器构成。
 * 抓取模块通过调用这些抓取器执行相关的采集动作，从而完成数据的采集与清晰。
 * 
 * 通常，采集相关的业务逻辑（抓取、清晰、入库）都是在抓取器中完成，
 * 抓取模块仅作为该采集任务的入口和行为调用存在。
 * @author Administrator
 *
 */
public abstract class CrawlModule {

	protected CrawlConfig crawlConfig;
	
	private final long startTime;
	
	public CrawlModule() {
		startTime = System.currentTimeMillis();
	}
	
	/**
	 * 提供返回当前抓取站点域名，CrawlModule子类实现
	 * @return
	 */
	public String getDomain(){
		//nobody.
		return null;
	}
	
	public abstract void execute();

	public DatatableOperator getDatatableOperator(Map<String, Object> param){
		return null;
	}
	public CrawlConfig getCrawlConfig() {
		return crawlConfig;
	}

	public void setCrawlConfig(CrawlConfig crawlConfig) {
		this.crawlConfig = crawlConfig;
	}
	
	public long getStartTime() {
		return startTime;
	}

	public File getDataDir() {
		String moduleHome = System.getProperty(SystemConstant.MODULE_HOME_KEY);
		File dataDir = new File(moduleHome, SystemConstant.MODULE_DATA_DIR_NAME);
		if (!dataDir.exists()) {
			dataDir.mkdirs();
		}
		return dataDir;
	}
	
	protected DatatableConfig initDatatableConfig(String tableName) {
		DatatableConfig datatableConfig = DatatableConfig.createDatatableConfigByXml(tableName);
		datatableConfig.buildConfig();
		return datatableConfig;
	}
}
