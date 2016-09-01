package com.sxit.crawler.utils;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;

public abstract class DomUtils {

	public String getElementValue(Element element, String type, String attrName) {
		String text = null;
//		if (null == element) 
//			return text;
//		if (ExtrDomRule.TYPE_HTML.equalsIgnoreCase(type)) {
//			text = element.html();
//		} else if (ExtrDomRule.TYPE_OUTHTML.equalsIgnoreCase(type)) {
//			text = element.outerHtml();
//		} else if (ExtrDomRule.TYPE_TEXT.equalsIgnoreCase(type)) {
//			text = element.text();
//		} else if (ExtrDomRule.TYPE_ATTR.equalsIgnoreCase(type)) {
//			text = element.attr(attrName);
//		} else {
//			text = element.html();
//			if (StringUtils.isBlank(text)) {
//				text = element.outerHtml();
//			}
//		}
		return text;
	}
}
