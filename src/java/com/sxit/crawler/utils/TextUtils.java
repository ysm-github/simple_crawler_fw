package com.sxit.crawler.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public abstract class TextUtils {

	private final static Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

	/**
	 * 从内容中提取出数字
	 * 
	 * @param srcStr
	 * @return
	 */
	public static int extrNumber(String srcStr) {
		if (StringUtils.isNotBlank(srcStr)) {
			Matcher matcher = NUMBER_PATTERN.matcher(srcStr);
			if (matcher.find()) {
				try {
					return Integer.parseInt(matcher.group());
				} catch (NumberFormatException e) {
				}
			}
		}
		return -1;
	}

	/**
	 * 将给定的数值类型转换为String
	 * 
	 * @param val
	 * @return
	 */
	public static String getStringValue(Object val) {
		String str = null;
		if (null == val)
			return null;

		if (val instanceof Double) {
			str = String.valueOf(((Double) val).longValue());
		} else if (val instanceof Integer) {
			str = String.valueOf(((Integer) val).intValue());
		} else if (val instanceof Long) {
			str = String.valueOf(((Integer) val).longValue());
		} else if (val instanceof BigDecimal) {
			str = String.valueOf(((BigDecimal) val).longValue());
		} else if (val instanceof String) {
			str = (String) val;
		} else {
			str = val + "";
		}
		return str;
	}

	/**
	 * 从字符串中截取中间字符串，并删除某个结果字符串
	 * 
	 * @param str
	 *            需要处理的字符串
	 * @param open
	 *            开始字符串
	 * @param close
	 *            结束字符串
	 * @param remove
	 *            从结果变量中需要删除的字符串
	 * @return
	 */
	public static String substringBetweenAndRemove(String str, String open,
			String close, String remove) {
		return substringBetweenAndRemoves(str, open, close,
				new String[] { remove });
	}

	/**
	 * 从字符串中截取中间字符串，并删除某个结果字符串
	 * 
	 * @param str
	 *            需要处理的字符串
	 * @param open
	 *            开始字符串
	 * @param close
	 *            结束字符串
	 * @param removes
	 *            从结果变量中需要删除的字符串,支持任意个
	 * @return
	 */
	public static String substringBetweenAndRemoves(String str, String open,
			String close, String removes[]) {
		String subStr = null;
		try {
			subStr = StringUtils.substringBetween(str, open, close);
			if (StringUtils.isNotBlank(subStr)) {
				subStr = subStr.trim();
				if (null != removes) {
					for (String remove : removes) {
						if (null != remove) {
							subStr = StringUtils.remove(subStr, remove);
						}
					}
				}
			}
		} catch (Exception e) {
		}

		return subStr;
	}

	/**
	 * 依据正则表达式提取
	 * 
	 * @param pattern
	 *            正则表达式
	 * @param text
	 *            文本
	 * @param groupIdx
	 *            获取匹配数据的组顺序，如果小于0则获取最后一个组
	 * @return
	 */
	public static String extrValueByRegx(Pattern pattern, String text,
			int groupIdx) {
		Matcher matcher = pattern.matcher(text);
		String str = null;
		boolean f = matcher.find();
		if (f && matcher.groupCount() > 0) {
			if (groupIdx <= 0) {
				str = matcher.group(matcher.groupCount());
			} else {
				str = matcher.group(groupIdx);
			}
		}
		return str;
	}

	/**
	 * 依据正则表达式提取
	 * 
	 * @param pattern
	 * @param text
	 * @return
	 */
	public static String extrValueByRegx(Pattern pattern, String text) {
		return extrValueByRegx(pattern, text, -1);
	}

	/**
	 * 依据正则表达式提取
	 * 
	 * @param regex
	 * @param text
	 * @return
	 */
	public static String extrValueByRegx(String regex, String text) {
		return extrValueByRegx(Pattern.compile(regex), text);
	}

	/**
	 * 依据正则表达式提取
	 * 
	 * @param regex
	 *            正则表达式字符串
	 * @param text
	 * @param groupIdx
	 * @return
	 */
	public static String extrValueByRegx(String regex, String text, int groupIdx) {
		return extrValueByRegx(Pattern.compile(regex), text, groupIdx);
	}

	/**
	 * 依据正则表达式提取所有匹配的字符
	 * 
	 * @param pattern
	 * @param text
	 * @param groupIdx
	 * @return
	 */
	public static List<String> extrValuesByRegx(Pattern pattern, String text,
			int groupIdx) {
		List<String> result = new ArrayList<String>();
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			if (matcher.groupCount() > 0) {
				if (groupIdx <= 0) {
					result.add(matcher.group(matcher.groupCount()));
				} else {
					result.add(matcher.group(groupIdx));
				}
			}
		}
		return result;
	}

	/**
	 * 依据正则提取所有匹配的组
	 * 
	 * @param pattern
	 * @param text
	 * @return
	 */
	public static List<String[]> extrValuesByRegxGroup(Pattern pattern,
			String text) {
		List<String[]> result = new ArrayList<String[]>();
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			if (matcher.groupCount() > 0) {
				String groups[] = new String[matcher.groupCount()];
				for (int i = 1; i <= matcher.groupCount(); i++) {
					groups[i - 1] = matcher.group(i);
				}
				result.add(groups);
			}
		}
		return result;
	}

	public static List<String> extrValuesByRegx(String regex, String text,
			int groupIdx) {
		return extrValuesByRegx(Pattern.compile(regex), text, groupIdx);
	}

	public static List<String[]> extrValuesByRegxGroup(String regex, String text) {
		return extrValuesByRegxGroup(Pattern.compile(regex), text);
	}
}
