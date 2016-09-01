package com.sxit.crawler.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

/**
 * 
 * @author Administrator
 *
 */
public class JsoupUtils {

//	/**
//	 * 过滤特殊字符
//	 * @param text
//	 * @return
//	 */
//	public static String replateSomeChar(String text) {
//		text = text.replaceAll(Jsoup.parse("&nbsp").text(), " ");
//	}
	
	public static String extrAttr(Element elt, String attributeKey) {
		if (null != elt) {
			return elt.attr(attributeKey);
		} else {
			return null;
		}
	}
	
	public static String extrFirstAttr(Element elt, String query, String attributeKey) {
		return extrAttr(extrFirstElt(elt, query), attributeKey);
	}
	
	public static String extrLastAttr(Element elt, String query, String attributeKey) {
		return extrAttr(extrLastElt(elt, query), attributeKey);
	}
	
	public static String extrAttrByIdx(Element elt, String query, String attributeKey, int idx) {
		return extrAttr(extrEltByIdx(elt, query, idx), attributeKey);
	}


	public static String extrHtml(Element elt) {
		if (null != elt) {
			return elt.html();
		} else {
			return null;
		}
	}
	
	public static String extrFirstHtml(Element elt, String query) {
		return extrHtml(extrFirstElt(elt, query));
	}
	
	public static String extrLastHtml(Element elt, String query) {
		return extrHtml(extrLastElt(elt, query));
	}
	
	public static String extrHtmlByIdx(Element elt, String query, int idx) {
		return extrHtml(extrEltByIdx(elt, query, idx));
	}
	
	public static String extrText(Element elt) {
		if (null != elt) {
			return elt.text();
		} else {
			return null;
		}
	}
	
	public static String extrFirstText(Element elt, String query) {
		return extrText(extrFirstElt(elt, query));
	}
	
	public static String extrLastText(Element elt, String query) {
		return extrText(extrLastElt(elt, query));
	}
	
	public static String extrTextByIdx(Element elt, String query, int idx) {
		return extrText(extrEltByIdx(elt, query, idx));
	}
	
	
	public static Element extrFirstElt(Element elt, String query) {
		if  (null != elt) {
			Elements select = elt.select(query);
			return null != select ? select.first() : null;
		}
		return null;
	}
	
	public static Element extrLastElt(Element elt, String query) {
		if  (null != elt) {
			Elements select = elt.select(query);
			return null != select ? select.last() : null;
		}
		return null;
	}
	
	public static Element extrEltByIdx(Element elt, String query, int idx) {
		if (null != elt) {
			Elements select = elt.select(query);
			if (!CollectionUtils.isEmpty(select) && idx < select.size()) {
				return select.get(idx);
			}
		}
		return null;
	}
	
}
