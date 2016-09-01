package com.sxit.crawler.commons.exception;

public interface PageProcessErrorCodes {

	/**
	 * 页面内容为空
	 */
	public static final int EXTR_PAGE_EMPTY = 0;
	
	/** 萃取位置错误（可能是页面代码发生变更） */
    public static final int EXTR_INDEX_ERROR = 1;
    
    /**
     * 萃取关键内容错误
     */
    public static final int EXTR_MAIN_CONTENT_ERROR = 2;
    
}
