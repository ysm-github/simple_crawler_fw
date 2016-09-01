package com.sxit.crawler.core.fetch;

import com.sxit.crawler.utils.UrlUtils;


public class FetchEntityBuilder {

	public static FetchEntry buildFetchEntry(String url) {
		FetchEntry fetchEntry = new FetchEntry();
		fetchEntry.setUrl(url);
		fetchEntry.setBaseUrl(UrlUtils.getBase(url));
		return fetchEntry;
	}
	public static FetchEntry buildFetchEntry(String url, UserAgentProvider userAgentProvider) {
		FetchEntry fetchEntry = new FetchEntry();
		fetchEntry.setUrl(url);
		fetchEntry.setUserAgentProvider(userAgentProvider);
		fetchEntry.setBaseUrl(UrlUtils.getBase(url));
		return fetchEntry;
	}
	
	public static FetchEntry buildFetchEntryByCookie(String url, String cookies) {
		FetchEntry fetchEntry = new FetchEntry();
		fetchEntry.setUrl(url);
		fetchEntry.setCookies(cookies);
		fetchEntry.setBaseUrl(UrlUtils.getBase(url));
		return fetchEntry;
	}
}
