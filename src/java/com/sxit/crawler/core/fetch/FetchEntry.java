package com.sxit.crawler.core.fetch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



/**
 * 抓取实例,主要包含如下数据项:
 * 1.包含待抓取的URL
 * 2.站点名称
 * 3.站点域名
 * 4.与站点树匹配的最终节点ID
 * @author exiang.liang
 * @version 1.0
 * @created 20-七月-2011 12:35:13
 */
public class FetchEntry {
	
	/**常量，用于标识队列中的开始*/
	public static final FetchEntry START_ENTRY = new FetchEntry();
	
	/**常量，用于标识队列中的结尾*/
	public static final FetchEntry END_ENTRY = new FetchEntry();
	
	
	public static enum FetchType { HTTP_GET, HTTP_POST, UNKNOWN };
	
	/**
     * 抓取类型
     */
    private transient FetchType fetchType = FetchType.UNKNOWN;
    
    /**
     * 附加参数
     */
    private final Map<String, Object> paramData = new ConcurrentHashMap<String, Object>(10);
    
    public void addParamData(String key, Object val) {
    	paramData.put(key, val);
    }
    
    public Object getParamData(String key) {
    	return paramData.get(key);
    }
    
	public FetchType getFetchType() {
		return fetchType;
	}

	public void setFetchType(FetchType fetchType) {
		this.fetchType = fetchType;
	}

	/**
	 * agent
	 */
	private UserAgentProvider userAgentProvider;

	/**
	 * 站点域名
	 */
	private String domain;
	/**
	 * 节点ID:与站点资源数相匹配的最后一个节点的ID
	 */
	private String nodeId;
	/**
	 * 站点ID:来自于站点资源树中的站点ID
	 */
	private String siteCode;
	
	/**
	 * 任务编码
	 */
	private String jobCode;
	
	/**
	 * 字符编码
	 */
	private String charset;  
	
	/**
	 * 待采集的URL
	 */
	private String url;
	
	private String baseUrl;


	/**
	 * 抓取结果
	 */
	private FetchResultEntry result;
	
	/**
	 * 是否结果页面
	 */
	private boolean isnodepage;

	private String cookies;
	
	public String getCookies() {
		return cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	public FetchEntry(){

	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	
	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public boolean isIsnodepage() {
		return isnodepage;
	}

	public void setIsnodepage(boolean isnodepage) {
		this.isnodepage = isnodepage;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public FetchResultEntry getResult() {
		return result;
	}

	public void setResult(FetchResultEntry result) {
		this.result = result;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	public UserAgentProvider getUserAgentProvider() {
		return userAgentProvider;
	}

	public void setUserAgentProvider(UserAgentProvider userAgentProvider) {
		this.userAgentProvider = userAgentProvider;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	@Override
	public String toString() {
		return "FetchEntry [userAgentProvider=" + userAgentProvider
				+ ", domain=" + domain + ", nodeId=" + nodeId + ", siteCode="
				+ siteCode + ", jobCode=" + jobCode + ", charset=" + charset
				+ ", url=" + url + ", isnodepage=" + isnodepage + ", cookies="
				+ cookies + "]";
	}


}//end FetchEntry