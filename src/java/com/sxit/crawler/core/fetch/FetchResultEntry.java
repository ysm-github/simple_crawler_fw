package com.sxit.crawler.core.fetch;

/**
 * 抓取结果，包含标志值、抓取起止时间、抓取的结果集合以及字符集
 * @author exiang.liang
 * @version 1.0
 * @created 20-七月-2011 12:21:42
 */
public class FetchResultEntry {

	/**
	 * 失败原因，对具体失败原因的详细描述
	 */
	private String failCause;
	/**
	 * 页面内容，抓取成功后的
	 */
	private StringBuffer pageContent;
	/**
	 * 抓取结果编码:0表示成功，其他值表示具体失败原因
	 */
	private int fetchResultCode;
	/**
	 * 完成下载时间戳
	 */
	private long endTimestamp = 0;
	/**
	 * 开始抓取时间戳
	 */
	private long beginTimestamp = 0;
	/**
	 * 字符集，表示下载网页的字符集
	 */
	private String charset;
	
	private String contentType;
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	private int fetchStatus = 0;    // default to unattempted

	public int getFetchStatus() {
		return fetchStatus;
	}

	public void setFetchStatus(int fetchStatus) {
		this.fetchStatus = fetchStatus;
	}

	public FetchResultEntry(){

	}

	public String getFailCause() {
		return failCause;
	}

	public void setFailCause(String failCause) {
		this.failCause = failCause;
	}

	public StringBuffer getPageContent() {
		return pageContent;
	}

	public void setPageContent(StringBuffer pageContent) {
		this.pageContent = pageContent;
	}

	public int getFetchResultCode() {
		return fetchResultCode;
	}

	public void setFetchResultCode(int fetchResultCode) {
		this.fetchResultCode = fetchResultCode;
	}

	public long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public long getBeginTimestamp() {
		return beginTimestamp;
	}

	public void setBeginTimestamp(long beginTimestamp) {
		this.beginTimestamp = beginTimestamp;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	public static String fetchStatusCodesToString(int code){
        switch(code){
            // DNS
            case FetchStatusCodes.S_DNS_SUCCESS : return "DNS-1-OK";
            // HTTP Informational 1xx
            case 100  : return "HTTP-100-Info-Continue";
            case 101  : return "HTTP-101-Info-Switching Protocols";
            // HTTP Successful 2xx
            case 200  : return "HTTP-200-Success-OK";
            case 201  : return "HTTP-201-Success-Created";
            case 202  : return "HTTP-202-Success-Accepted";
            case 203  : return "HTTP-203-Success-Non-Authoritative";
            case 204  : return "HTTP-204-Success-No Content ";
            case 205  : return "HTTP-205-Success-Reset Content";
            case 206  : return "HTTP-206-Success-Partial Content";
            // HTTP Redirection 3xx
            case 300  : return "HTTP-300-Redirect-Multiple Choices";
            case 301  : return "HTTP-301-Redirect-Moved Permanently";
            case 302  : return "HTTP-302-Redirect-Found";
            case 303  : return "HTTP-303-Redirect-See Other";
            case 304  : return "HTTP-304-Redirect-Not Modified";
            case 305  : return "HTTP-305-Redirect-Use Proxy";
            case 307  : return "HTTP-307-Redirect-Temporary Redirect";
            // HTTP Client Error 4xx
            case 400  : return "HTTP-400-ClientErr-Bad Request";
            case 401  : return "HTTP-401-ClientErr-Unauthorized";
            case 402  : return "HTTP-402-ClientErr-Payment Required";
            case 403  : return "HTTP-403-ClientErr-Forbidden";
            case 404  : return "HTTP-404-ClientErr-Not Found";
            case 405  : return "HTTP-405-ClientErr-Method Not Allowed";
            case 407  : return "HTTP-406-ClientErr-Not Acceptable";
            case 408  : return "HTTP-407-ClientErr-Proxy Authentication Required";
            case 409  : return "HTTP-408-ClientErr-Request Timeout";
            case 410  : return "HTTP-409-ClientErr-Conflict";
            case 406  : return "HTTP-410-ClientErr-Gone";
            case 411  : return "HTTP-411-ClientErr-Length Required";
            case 412  : return "HTTP-412-ClientErr-Precondition Failed";
            case 413  : return "HTTP-413-ClientErr-Request Entity Too Large";
            case 414  : return "HTTP-414-ClientErr-Request-URI Too Long";
            case 415  : return "HTTP-415-ClientErr-Unsupported Media Type";
            case 416  : return "HTTP-416-ClientErr-Requested Range Not Satisfiable";
            case 417  : return "HTTP-417-ClientErr-Expectation Failed";
            // HTTP Server Error 5xx
            case 500  : return "HTTP-500-ServerErr-Internal Server Error";
            case 501  : return "HTTP-501-ServerErr-Not Implemented";
            case 502  : return "HTTP-502-ServerErr-Bad Gateway";
            case 503  : return "HTTP-503-ServerErr-Service Unavailable";
            case 504  : return "HTTP-504-ServerErr-Gateway Timeout";
            case 505  : return "HTTP-505-ServerErr-HTTP Version Not Supported";
            // TreeCrawler internal codes (all negative numbers
            case FetchStatusCodes.S_BLOCKED_BY_USER:
                return "TreeCrawler(" + FetchStatusCodes.S_BLOCKED_BY_USER + ")-Blocked by user";
            case FetchStatusCodes.S_BLOCKED_BY_CUSTOM_PROCESSOR:
                return "TreeCrawler(" + FetchStatusCodes.S_BLOCKED_BY_CUSTOM_PROCESSOR +
                ")-Blocked by custom prefetch processor";
            case FetchStatusCodes.S_DELETED_BY_USER:
                return "TreeCrawler(" + FetchStatusCodes.S_DELETED_BY_USER + ")-Deleted by user";
            case FetchStatusCodes.S_CONNECT_FAILED:
                return "TreeCrawler(" + FetchStatusCodes.S_CONNECT_FAILED + ")-Connection failed";
            case FetchStatusCodes.S_CONNECT_LOST:
                return "TreeCrawler(" + FetchStatusCodes.S_CONNECT_LOST + ")-Connection lost";
            case FetchStatusCodes.S_DEEMED_CHAFF:
                return "TreeCrawler(" + FetchStatusCodes.S_DEEMED_CHAFF + ")-Deemed chaff";
            case FetchStatusCodes.S_DEFERRED:
                return "TreeCrawler(" + FetchStatusCodes.S_DEFERRED + ")-Deferred";
            case FetchStatusCodes.S_DOMAIN_UNRESOLVABLE:
                return "TreeCrawler(" + FetchStatusCodes.S_DOMAIN_UNRESOLVABLE
                        + ")-Domain unresolvable";
            case FetchStatusCodes.S_OUT_OF_SCOPE:
                return "TreeCrawler(" + FetchStatusCodes.S_OUT_OF_SCOPE + ")-Out of scope";
            case FetchStatusCodes.S_DOMAIN_PREREQUISITE_FAILURE:
                return "TreeCrawler(" + FetchStatusCodes.S_DOMAIN_PREREQUISITE_FAILURE
                        + ")-Domain prerequisite failure";
            case FetchStatusCodes.S_ROBOTS_PREREQUISITE_FAILURE:
                return "TreeCrawler(" + FetchStatusCodes.S_ROBOTS_PREREQUISITE_FAILURE
                        + ")-Robots prerequisite failure";
            case FetchStatusCodes.S_OTHER_PREREQUISITE_FAILURE:
                return "TreeCrawler(" + FetchStatusCodes.S_OTHER_PREREQUISITE_FAILURE
                        + ")-Other prerequisite failure";
            case FetchStatusCodes.S_PREREQUISITE_UNSCHEDULABLE_FAILURE:
                return "TreeCrawler(" + FetchStatusCodes.S_PREREQUISITE_UNSCHEDULABLE_FAILURE
                        + ")-Prerequisite unschedulable failure";
            case FetchStatusCodes.S_ROBOTS_PRECLUDED:
                return "TreeCrawler(" + FetchStatusCodes.S_ROBOTS_PRECLUDED + ")-Robots precluded";
            case FetchStatusCodes.S_RUNTIME_EXCEPTION:
                return "TreeCrawler(" + FetchStatusCodes.S_RUNTIME_EXCEPTION
                        + ")-Runtime exception";
            case FetchStatusCodes.S_SERIOUS_ERROR:
                return "TreeCrawler(" + FetchStatusCodes.S_SERIOUS_ERROR + ")-Serious error";
            case FetchStatusCodes.S_TIMEOUT:
                return "TreeCrawler(" + FetchStatusCodes.S_TIMEOUT + ")-Timeout";
            case FetchStatusCodes.S_TOO_MANY_EMBED_HOPS:
                return "TreeCrawler(" + FetchStatusCodes.S_TOO_MANY_EMBED_HOPS
                        + ")-Too many embed hops";
            case FetchStatusCodes.S_TOO_MANY_LINK_HOPS:
                return "TreeCrawler(" + FetchStatusCodes.S_TOO_MANY_LINK_HOPS
                        + ")-Too many link hops";
            case FetchStatusCodes.S_TOO_MANY_RETRIES:
                return "TreeCrawler(" + FetchStatusCodes.S_TOO_MANY_RETRIES + ")-Too many retries";
            case FetchStatusCodes.S_UNATTEMPTED:
                return "TreeCrawler(" + FetchStatusCodes.S_UNATTEMPTED + ")-Unattempted";
            case FetchStatusCodes.S_UNFETCHABLE_URI:
                return "TreeCrawler(" + FetchStatusCodes.S_UNFETCHABLE_URI + ")-Unfetchable URI";
            case FetchStatusCodes.S_PROCESSING_THREAD_KILLED:
                return "TreeCrawler(" + FetchStatusCodes.S_PROCESSING_THREAD_KILLED + ")-" +
                    "Processing thread killed";
            // Unknown return code
            default : return Integer.toString(code);
        }
    }

	@Override
	public String toString() {
		return "FetchResultEntry [failCause=" + failCause + ", pageContent="
				+ pageContent + ", fetchResultCode=" + fetchResultCode
				+ ", endTimestamp=" + endTimestamp + ", beginTimestamp="
				+ beginTimestamp + ", charset=" + charset + ", contentType="
				+ contentType + ", fetchStatus=" + fetchStatus + "]";
	}
	
}//end FetchResultEntry