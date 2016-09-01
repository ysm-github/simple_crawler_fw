package com.sxit.crawler.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.cache.CachingHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sxit.crawler.commons.exception.ConnectTimeOutException;
import com.sxit.crawler.commons.exception.ReadTimeOutException;
import com.sxit.crawler.core.fetch.FetchEntityBuilder;
import com.sxit.crawler.core.fetch.FetchEntry;
import com.sxit.crawler.core.fetch.FetchHTTP;


/**
 * <strong>CrawlerUtils</strong><br>
 * <p>
 * 
 * </p>
 * @since 0.1
 * @version $Id: CrawlerUtils.java,v 0.1 2013-10-18 下午2:36:23 lex Exp $
 */
public class CrawlerUtils {

	private static Logger log = LoggerFactory.getLogger(CrawlerUtils.class);
	
	private final static String DEFAULT_USER_AGENT="Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)";;
	
	public static void main(String[] args) {
		//<a href="http://store.taobao.com/shop/view_shop.htm?user_number_id=901409638&amp;rn=c580cc64c644dd7d16c302aa328a5829" atpanel="1-3,901409638,,,shop-title,2,title,901409638" class="sHe-shop">进入店铺<i>&gt;&gt;</i></a>
//		String url = "http://store.taobao.com/shop/view_shop.htm?user_number_id=901409638&rn=c580cc64c644dd7d16c302aa328a5829";
//		FetchEntry fetchEntry = FetchEntityBuilder.buildFetchEntry(url);
//		String shopUrl = extrRedirectHref(fetchEntry);
//		System.out.println(shopUrl);
//		fetchEntry = FetchEntityBuilder.buildFetchEntry(shopUrl);
//		String shopId = TextUtils.substringBetweenAndRemove(shopUrl, "http://shop", ".", "");
//		shopUrl = extrRedirectHref(fetchEntry);
//		System.out.println(shopUrl);
//		System.out.println(shopId);
//		String url = "http://store.taobao.com/shop/view_shop.htm?user_number_id=901409638&rn=c580cc64c644dd7d16c302aa328a5829";
//		String userid = TextUtils.extrValueByRegx("[\\S]*?user_number_id=([\\d]*)[\\S]*", url);
//		System.out.println(userid);
		System.out.println(extrRedirectHref(FetchEntityBuilder.buildFetchEntry("http://vinus.tmall.com")));
	}
	
	public static String extrRedirectHref(FetchEntry fetchEntry) {
		DefaultHttpClient httpclient=null;
		
		httpclient= new DefaultHttpClient();
		if (null != fetchEntry.getUserAgentProvider()) {
			httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, fetchEntry.getUserAgentProvider().getUserAgent());
		} else {
			httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, DEFAULT_USER_AGENT);
		}
		httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);//设置建立连接超时
		httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);//设置读取数据超时
		
		httpclient.setRedirectStrategy(new RedirectStrategy() {

			//@Override
			public boolean isRedirected(HttpRequest request,
					HttpResponse response, HttpContext context)
					throws ProtocolException {
				return false;
			}

			//@Override
			public HttpUriRequest getRedirect(HttpRequest request,
					HttpResponse response, HttpContext context)
					throws ProtocolException {
				return null;
			}
		});
		
		try {
			HttpGet httpget = new HttpGet(fetchEntry.getUrl());
			if(StringUtils.isNotBlank(fetchEntry.getCookies())){
				httpget.setHeader("cookie", fetchEntry.getCookies());//cookies非空则此处加cookies
				if (log.isDebugEnabled()) {
					log.info("USE COOKIE:{}", fetchEntry.getCookies());
				}
			}
			HttpResponse httpResponse = httpclient.execute(httpget);
			
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == statusCode) {
				return fetchEntry.getUrl();
			} else if (HttpStatus.SC_MOVED_PERMANENTLY == statusCode || HttpStatus.SC_MOVED_TEMPORARILY == statusCode) {
				Header[] headers = httpResponse.getHeaders("Location");
				if (null != headers && headers.length > 0) {
					String redirectUrl = headers[0].getValue();
					return redirectUrl;
				}
			}
			
			
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
		return null;
	}
}
