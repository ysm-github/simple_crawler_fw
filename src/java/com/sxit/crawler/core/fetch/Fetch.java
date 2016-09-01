package com.sxit.crawler.core.fetch;


/**
 *  httpclient下载接口
 */

public interface Fetch {
	/**
	 * url参数格式为http://xxx.xxx.xxx
	 * 返回结果对象包含开始抓取时间、结束时间、内容、url地址、字符编码等
	 */
	FetchEntry process(FetchEntry fetchEntry);
	
}