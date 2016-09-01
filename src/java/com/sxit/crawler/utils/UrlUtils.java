package com.sxit.crawler.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sxit.crawler.core.fetch.FetchEntry;

public class UrlUtils {
	public static List<String> findUrl(FetchEntry fetchEntry){
		String url = fetchEntry.getUrl();
		if (StringUtils.isBlank(fetchEntry.getDomain())) {
			return null;
		}
		int idx = url.indexOf(fetchEntry.getDomain());
		if (idx <= 0) {//站外
			return null;
		}
		List<String> list=new ArrayList<String>();
		Document doc = Jsoup.parse(fetchEntry.getResult().getPageContent().toString().trim(), url);
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			String linkHref = link.attr("href");
			// 第四步：判断url是否为目标网页
			if (StringUtils.contains(linkHref, fetchEntry.getDomain())) {
				list.add(linkHref);
			}
		}
		Elements iframeLinks = doc.select("iframe[src]");
		for (Element link : iframeLinks) {
			String linkHref = link.attr("src");
			// 第四步：判断url是否为目标网页
			if (StringUtils.contains(linkHref, fetchEntry.getDomain())) {
				list.add(linkHref);
			}
		}
		return list;
	}
	/**
	 *  判断url是否正确，为true表示是正确的url
	 *  此方法可以根据实际需要继续添加条件
	 */
	public static boolean ifCorrectUrl(String url){
		String tmp=url.toLowerCase();
		//判断是否有javascript跳转
		if(tmp.indexOf("javascript")!=-1){
//			System.out.println("1:"+tmp.indexOf("javascript"));
			return false;
		}
		//判断是否有window.open跳转
		if(tmp.indexOf("window.open")!=-1){
//			System.out.println("2:"+tmp.indexOf("window.open"));
			return false;
		}
		//判断是否有多个http://
		if(tmp.lastIndexOf("http://")>0){
//			System.out.println("3:"+tmp.lastIndexOf("http://"));
			return false;
		}
		
		return true;
		
	}
	
	public static String getBase(String url) {
		if (StringUtils.isNotBlank(url)) {
			int firstIdx = url.indexOf("://");
			if (firstIdx < 0) {
				firstIdx = 0;
			}
			int idx = url.indexOf("/", firstIdx+3+1);
			if (idx != -1) {
				return url.substring(0, idx);
			} else {
				return url;
			}
		}
		return url;
		
	}
	
	/**
	 *   去掉#号,可以扩展其它方法
	 */
	public static String updateUrl(String oldUrl){
		if(StringUtils.contains(oldUrl, "#")){
			return oldUrl.substring(0, oldUrl.indexOf("#"));
		}
		return oldUrl;
	}
	
	public static String encodeUrl(String url, String charset) {
		try {
			return URLEncoder.encode(url, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	public static String decodeUrl(String url, String charset) {
		try {
			return URLDecoder.decode(url, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	public static void main(String[] args) {
		String url = "http://mvnrepository.com/artifact/org.apache.avro";
		System.out.println(getBase(url));
	}
}
