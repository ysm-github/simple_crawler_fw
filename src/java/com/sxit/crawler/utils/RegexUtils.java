package com.sxit.crawler.utils;

import com.sxit.crawler.core.fetch.FetchEntityBuilder;
import com.sxit.crawler.core.fetch.FetchEntry;
import com.sxit.crawler.core.fetch.FetchHTTP;


/**
 * <strong>RegixUtils</strong><br>
 * <p>
 *
 * </p>
 * @since 0.1
 * @version $Id: RegixUtils.java,v 0.1 2013-10-16 下午6:15:14 lex Exp $
 */
public class RegexUtils {

	public static String testRegex(String url, String regex, int groupIdx) {
		FetchEntry fetchEntry = FetchEntityBuilder.buildFetchEntry(url);
		FetchHTTP fetchHTTP = new FetchHTTP();
		fetchEntry = fetchHTTP.process(fetchEntry);
		if (null != fetchEntry && null != fetchEntry.getResult()) {
			String html = fetchEntry.getResult().getPageContent().toString();
			return TextUtils.extrValueByRegx(regex, html, groupIdx);
		}
		return null;
			
	}
}
