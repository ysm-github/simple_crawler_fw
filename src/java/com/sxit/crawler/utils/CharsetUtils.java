package com.sxit.crawler.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 分析出网页编码
 */
public class CharsetUtils {
	// 获取网页编码，防止乱码_开始
	public static String charset(String xx) {
		String x = xx;
		String bianma = "";
		Pattern p;
		Matcher m;
		String z = "<meta[^>]*?charset=[^>]*?>";
		p = Pattern.compile(z);
		m = p.matcher(x);
		if (m.find()) {
			String zz = m.group();
			zz = zz.substring(zz.indexOf("charset=") + 8);
			zz = zz.trim();
			String[] zz1 = zz.split("[ \"/]");
			if (zz1[0] != null) {
				if (zz1[0].length() > 2) {
					bianma = zz1[0];
				}
			}
			if(bianma==null || bianma.equals("")){return "ISO-8859-1";}
			bianma = bianma.toLowerCase();
		}else{
			bianma = "ISO-8859-1";
			}
		return bianma;
	}
	// 获取网页编码，防止乱码_结束
}
