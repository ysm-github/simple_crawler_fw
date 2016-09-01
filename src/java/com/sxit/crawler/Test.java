package com.sxit.crawler;

import java.net.SocketTimeoutException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
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
import com.sxit.crawler.core.fetch.FetchResultEntry;

public class Test {
    private static final int REQUEST_TIMEOUT = 3 * 1000; // 设置请求超时10秒钟
    private static final int TIMEOUT         = 6 * 1000; // 连接超时时间
    private static final int SO_TIMEOUT      = 6 * 1000; // 数据传输超时
    private static final String CHARSET      = "UTF-8";
	private static Logger log = LoggerFactory.getLogger(Test.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String url="http://big.cr173.com/Oracle.10g.For.Windows.7z";
		HttpClient httpclient=null;
		httpclient= new DefaultHttpClient();
		;
//		httpclient.getHostConfiguration().setProxy(fetchEntry.getParamData("proxy.ip"), (Integer)fetchEntry.getParamData("proxy.prot"));  
//		httpclient.getParams().setIntParameter(CoreConnectionPNames., arg1)
		httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);//设置建立连接超时
		httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);//设置读取数据超时
		if (log.isDebugEnabled()) {
			log.info("抓取网页开始------->");
		}
		Date starttime=new Date();
		try {
			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = httpclient.execute(httpget);
			printHeaders(httpResponse);
			 long responseLength = 0; // 响应长度
		        String responseContent = null; // 响应内容
		        String strRep = null;
            // 获取响应消息实体
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) 
            {
                responseLength = entity.getContentLength();
                responseContent = EntityUtils.toString(entity, CHARSET);//不能重复调用此方法，IO流已关闭。
                
                System.err.println("内容编码: " + entity.getContentEncoding());
                System.err.println("请求地址: " + httpget.getURI());
                System.err.println("响应状态: " + httpResponse.getStatusLine());
                System.err.println("响应长度: " + responseLength);
                System.err.println("响应内容: \r\n" + responseContent);
                
                // 获取HTTP响应的状态码
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK)
                {
                    strRep = responseContent; // EntityUtils.toString(httpResponse.getEntity());
                }
                
                // Consume response content
                EntityUtils.consume(entity);
                // Do not need the rest
                httpget.abort();
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
	}
	 // 打印头信息
    private static void printHeaders(HttpResponse httpResponse)
    {
        System.out.println("------------------------------");
        // 头信息
        HeaderIterator it = httpResponse.headerIterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        System.out.println("------------------------------");
    }
    


}
