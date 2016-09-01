package com.sxit.crawler.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 日期操作工具类
 * @since 1.0
 * @version $Id: DateUtils.java,v 1.2 2010/03/21 14:10:07 liangexiang Exp $
 */
public abstract class DateUtils {

	public static final String[] weeks = new String[] {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六",  "星期天"};
	
	public static final ThreadLocal<SimpleDateFormat> DATA_FORMAT_14 = threadLocalDateFormat("yyyyMMddHHmmss");
	public static final ThreadLocal<SimpleDateFormat> YYYY_MM_DD = threadLocalDateFormat("yyyy-MM-dd");
	
    private static ThreadLocal<SimpleDateFormat> threadLocalDateFormat(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<SimpleDateFormat>() {
            protected SimpleDateFormat initialValue() {
                SimpleDateFormat df = new SimpleDateFormat(pattern);
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                return df;
            }
        };
        return tl;
    }
	
	public synchronized static Date setDateHour(Date date, int hour) {
		if (null != date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime();
		}
		return null;
	}
	
	public static Date dateAdd(Date date, int day) {
		if (null != date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_YEAR, day);
			return calendar.getTime();
		}
		return null;
	}
	
	/**
	 * 将日期中的时间去掉
	 * @param date
	 * @return
	 */
	public static Date removeTimeValue(Date date) {
		if (null != date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime();
		}
		return null;
	}
	
	/**
	 * 获取一天中最小时间
	 * @param date
	 * @return
	 */
	public static Date getMinTimeOfDay(Date date) {
		if (null != date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime();
		}
		return null;
	}
	
	/**
	 * 获取一天中最大时间
	 * @param date
	 * @return
	 */
	public static Date getMaxTimeOfDay(Date date) {
		if (null != date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			return calendar.getTime();
		}
		return null;
	}
	
	public static Date getDateForWeekNum(Date date, int weekNum) {
		if (null == date) {
			date = new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY); 
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY+weekNum);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
		
	}
	
	/**
	 * 获取当前天所处的星期值，周的起始日期为星期一
	 * @param date
	 * @return 星期一：1、星期二：2，以此类推。
	 */
	public static int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);  
		cal.setFirstDayOfWeek(Calendar.MONDAY);  
		int tmp = cal.get(Calendar.DAY_OF_WEEK) - 1;  
		if (0 == tmp) {  
		    tmp = 7;  
		}
		return tmp;
	}
	
	/**
	 * 获取指定日期的周数
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);  
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * 获取当前天所处的星期值可读形式，周的起始日期为星期一
	 * @param date
	 * @return 星期一、星期二、星期三
	 */
	public static String getDayOfWeekStr(Date date) {
		int tmp = getDayOfWeek(date);
		if (tmp <= weeks.length) {
			return weeks[tmp-1];
		}
		return "";
	}
	
	/**
	 * 获取指定日期所对应的星期开始时间
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY); 
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static Date getLastDateOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);  
		cal.setFirstDayOfWeek(Calendar.MONDAY); 
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY+1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 获取最近num周的起始日期列表
	 * @param date 指定的日期（以此为最后日期）
	 * @param num 最近周数（比如最近两周、最近3周）
	 * @return 周的起始日期列表
	 */
	public static List<Date> getLastWeeks(Date date, int num) {
		Calendar calendar = Calendar.getInstance();
		List<Date> lastWeekDate = new ArrayList<Date>();
		Date tmpDate = new Date(date.getTime());
		for (int i=0; i<num; i++) {
			calendar.setTime(tmpDate);
			calendar.add(Calendar.WEEK_OF_YEAR, i*(-1));
			lastWeekDate.add(getFirstDateOfWeek(calendar.getTime()));
		}
		return lastWeekDate;
	}
	

	/**
	 * 获取日期所对应的月份数(0开始计数)
	 * @param date
	 * @return
	 */
	public static int getMonthNum(Date date) {
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);  
		return cal.get(Calendar.MONTH);
	}
	
	public static int getYearNum(Date date) {
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);  
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * 获取一个月中的第一天
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取一个月中的最后一天
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	public static String getCurrentDateStr(DateFormat formate) {
		Date date = new Date();
		return formate.format(date);
	}
	
	public static String getCurrentDateStr() {
		return getCurrentDateStr(DATA_FORMAT_14.get());
	}
	public static String getDateStrByDefault(Date date) {
		return getDateStr(date, YYYY_MM_DD.get());
	}
	
	public static Date getDateByStr(String dateStr) {
		return getDateByStr(dateStr, DATA_FORMAT_14.get());
	}
	
	public static String get14DigitDate(Date date) {
		return getDateStr(date, DATA_FORMAT_14.get());
	}
	
	public static String getDateStr(Date date, DateFormat formate) {
		synchronized (formate) {
			return formate.format(date);
		}
	}
	

	public static Date getDateByStr(String dateStr, DateFormat formate) {
		synchronized (formate) {
			try {
				return formate.parse(dateStr);
			} catch (ParseException e) {
				return null;
			}
		}
	}
	
	public static boolean compareDate(String datumTime,String comparativeTime,DateFormat format){
		Calendar datumTimeCalendar = Calendar.getInstance();
		Calendar comparativeTimeCalenbar = Calendar.getInstance();
		datumTimeCalendar.setTime(getDateByStr(datumTime,format));
		comparativeTimeCalenbar.setTime(getDateByStr(comparativeTime,format));
		int result = datumTimeCalendar.compareTo(comparativeTimeCalenbar);
		if (result == 0) {
			return true;
		}
		if (result < 0 ) {
			return true;
		}
		if (result > 0) {
			return false;
		}
		return false;
	}
	
}
