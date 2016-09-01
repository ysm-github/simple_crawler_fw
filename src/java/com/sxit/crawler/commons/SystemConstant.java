package com.sxit.crawler.commons;

public class SystemConstant {
	
	private boolean debug = false;
	
	public final static String MODULE_HOME_KEY = "module.home";
	
	public final static String APP_HOME_KEY = "app.home";
	
	public final static String APP_NAME_KEY = "app.name";
	
	public final static String MODULE_DATA_DIR_NAME = "data";
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
}
