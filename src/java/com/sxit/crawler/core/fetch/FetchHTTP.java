package com.sxit.crawler.core.fetch;

import java.net.SocketTimeoutException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sxit.crawler.commons.exception.ConnectTimeOutException;
import com.sxit.crawler.commons.exception.ReadTimeOutException;
import com.sxit.crawler.utils.CharsetUtils;
import com.sxit.crawler.utils.EncodingDetector;
import com.sxit.crawler.utils.UrlUtils;


public class FetchHTTP implements Fetch{

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private final static String DEFAULT_USER_AGENT="Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)";;
	
	public FetchEntry process(FetchEntry fetchEntry) {
		FetchEntry result= fetchEntry;
		HttpClient httpclient=null;
		httpclient= new DefaultHttpClient();
		if (null != fetchEntry.getUserAgentProvider()) {
			httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, fetchEntry.getUserAgentProvider().getUserAgent());
		} else {
			httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, DEFAULT_USER_AGENT);
		}
		;
//		httpclient.getHostConfiguration().setProxy(fetchEntry.getParamData("proxy.ip"), (Integer)fetchEntry.getParamData("proxy.prot"));  
//		httpclient.getParams().setIntParameter(CoreConnectionPNames., arg1)
		httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);//设置建立连接超时
		httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);//设置读取数据超时
		if (log.isDebugEnabled()) {
			log.info("抓取网页开始------->");
		}
		Date starttime=new Date();
		try {
			HttpGet httpget = new HttpGet(fetchEntry.getUrl());
			if(StringUtils.isNotBlank(fetchEntry.getCookies())){
				httpget.setHeader("cookie", fetchEntry.getCookies());//cookies非空则此处加cookies
				if (log.isDebugEnabled()) {
					log.info("USE COOKIE:{}", fetchEntry.getCookies());
				}
			}
			HttpResponse response = httpclient.execute(httpget);
			FetchResultEntry fetchResult = responseProcess(response, fetchEntry.getUrl(), starttime,fetchEntry.getCharset());
			result.setResult(fetchResult);
		}catch (ConnectTimeoutException ex) {
			throw new ConnectTimeOutException("HttpClient Connection TimeOut! ",ex);
		}catch(SocketTimeoutException ex){
			throw new ReadTimeOutException("HttpClient Read TimeOut! ",ex);//读取数据超时异常
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			try{
				httpclient.getConnectionManager().shutdown();// 关闭httpclient连接
			}catch(Exception e){
				log.error("httpclient关闭出错",e);
			}
		}
		return result;
	}
	
	
	private FetchResultEntry responseProcess(HttpResponse response, String url, Date starttime,String charsetStr) {
		FetchResultEntry result=new FetchResultEntry();
		int statusCode=response.getStatusLine().getStatusCode();
		if(statusCode!=200){
			result.setFetchResultCode(statusCode);
			result.setFailCause("http连接出错，错误代码:"+statusCode);
			return result;
		}
		HttpEntity entity = response.getEntity();
		if (null == entity) {
			result.setFetchResultCode(statusCode);
			result.setFailCause("无法获取网页内容，网络问题:"+statusCode);
			return result;
		}
		result.setContentType(entity.getContentType().toString());
		
		//优先以HTTP返回头中的charset为准，当返回头的charset为null的时候，则使用指定的charsetStr值
		//如果HTTP返回头中没有charset；charsetStr值为也为空，则从页面中去分析出chareset来（即<meta>标签中的）
		String contentCharSet = EntityUtils.getContentCharSet(entity);
		String charset = null;//获取字符编码,若为空则解析charset，并转换字符编码
		if (StringUtils.isNotBlank(contentCharSet)) {
			charset = contentCharSet;
		} else {
			charset = charsetStr;
		}
		try {
			if (entity != null) {
				byte[] data = null;
				Header contentEncoding = entity.getContentEncoding();
				StringBuffer sb = null;
				
				if (null != contentEncoding && StringUtils.equalsIgnoreCase("gzip", contentEncoding.getValue())) {
					entity = new GzipDecompressingEntity(entity);
				} 
				data = EntityUtils.toByteArray(entity);
				if (StringUtils.isBlank(charset)) {
					//从页面中探测字符集
					String str = new String(data, "ISO-8859-1");
					charset = CharsetUtils.charset(str);
				}
				if (StringUtils.isBlank(charset)) {
					EncodingDetector encodingDetector = new EncodingDetector();
					encodingDetector.autoDetectClues(entity.getContentType().toString(), data, true);
					charset = encodingDetector.guessEncoding(UrlUtils.getBase(url), "ISO-8859-1");
				}
				if (StringUtils.isBlank(charset)) {
					
					charset  = "ISO-8859-1";
				}
				sb = new StringBuffer(new String(data, charset));
//				
//				
//				else {
//					
//					ByteArrayOutputStream baos = new ByteArrayOutputStream();
//					IOUtils.copyLarge(entity.getContent(), baos);
//					byte buff[] = baos.toByteArray();
//					if (null != buff && buff.length > 0) {
//						if (StringUtils.isBlank(charset)) {
//							StringBuffer cbf = new StringBuffer();
//							cbf.append(new String(buff, "ISO-8859-1"));
//							charset=CharsetUtils.charset(cbf.toString());
//							new WeakReference<StringBuffer>(cbf);
//							cbf = null;
//						}
//						if (StringUtils.isBlank(charset)) {
//							charset = "ISO-8859-1";
//						}
//						sb.append(new String(buff, charset));
//					}
//					new WeakReference<Object>(buff);
//					buff = null;
//				}
				long endTimestamp=System.currentTimeMillis();;
				result.setBeginTimestamp(starttime.getTime());
				result.setEndTimestamp(endTimestamp);
				result.setFetchResultCode(0);
				result.setCharset(charset);
				result.setPageContent(sb);
				if (log.isDebugEnabled()) {
					log.info("开始时间----->" + System.currentTimeMillis());
					log.info("返回状态码----->" + statusCode);
					log.info("字符编码----->" + charset);
					log.info("文件大小----->" + entity.getContentLength());
					log.info("URL地址----->" + url);
					log.info("抓取网页完成------->");
				}
			}
		} catch (Exception e) {
			log.error("Response处理出错:"+e.getMessage(), e);
			result.setFetchResultCode(800);
			result.setFailCause("http连接出错，错误代码:"+statusCode+"错误原因:Response类处理出错");
			return result;
		}
		return result;
	}

}
