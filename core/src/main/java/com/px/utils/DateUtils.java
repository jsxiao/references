package com.px.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.px.core.utils.Converts;
import com.px.core.utils.Validate;


public final class DateUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
	
	/**
	 * 日期格式化
	 * @param date 要格式化的日期
	 * @param f e.g: yyyyMMdd
	 * @return 格式化后的字符串
	 */
	public static String dateStr(Date date, String f) {
	    return Converts.DateToString(date, f); 
	}
	
	/**
	 * MM月dd日 hh:mm
	 * @param date
	 * @return
	 */
	public static String dateStr1(Date date) { 
		return dateStr(date, "MM月dd日 hh:mm"); 
    }

	/**
	 * yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String dateStr2(Date date) {
		return dateStr(date, "yyyy-MM-dd");
	}
  
	/**
	 * yyyyMMdd
	 * @param date
	 * @return
	 */
	public static String dateStr3(Date date) {
		return dateStr(date, "yyyyMMdd");
	}

	/**
	 * yyyy年MM月dd日
	 * @param date
	 * @return
	 */
	public static String dateStr4(Date date) {
		return dateStr(date, "yyyy年MM月dd日");
	}

	/**
	 * yyMMdd
	 * @param date
	 * @return
	 */
	public static String dateStr5(Date date) {
		return dateStr(date, "yyMMdd");
	}

	/**
	 * yyyyMMddHHmmss
	 * @param date
	 * @return
	 */
	public static String dateStr6(Date date) {
		return dateStr(date, "yyyyMMddHHmmss");
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String dateStr7(Date date) {
		return dateStr(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * yyyy-MM
	 * @param date
	 * @return
	 */
	public static String dateStr8(Date date) {
		return dateStr(date, "yyyy-MM");
	}

	/**
	 * MM-dd
	 * @param date
	 * @return
	 */
	public static String dateStr9(Date date) {
		return dateStr(date, "MM-dd");
	}

	/**
	 * HHmmss
	 * @param date
	 * @return
	 */
	public static String dateStr10(Date date) {
		return dateStr(date, "HHmmss");
	}

	/**
	 * 字符串转Date
	 * @param time
	 * @param formatStr
	 * @return
	 */
	public static Date getDate(String time, String formatStr) {
		return Converts.StrToDate(time, formatStr);
	}
	
	/**
	 * yyyyMMdd
	 * @param strDate
	 * @return
	 */
	public static Date getDate1(String strDate){
		return getDate(strDate, "yyyyMMdd");
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @param strDate
	 * @return
	 */
	public static Date getDate2(String strDate){
		return getDate(strDate, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 字符串转Date
	 * @param times 当前时间的毫秒
	 * @return
	 */
	public static Date getDate(String times) {
		long time = Long.parseLong(times);
		return new Date(time * 1000L);
	}

	/**
	 * 返回指定时间的秒数
	 * @param date
	 * @return
	 */
	public static long getTime(Date date) {
		return date.getTime() / 1000L;
	}

	/**
	 * 返回当前时间属于当月的哪天
	 * @param d
	 * @return
	 */
	public static int getDay(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return cal.get(Calendar.DATE);
	}


	public static Map<String, Integer> getApartTime(String Begin, String end) {
		String[] temp = Begin.split("-");
		String[] temp2 = end.split("-");
		if ((temp.length > 1) && (temp2.length > 1)) {
			Calendar ends = Calendar.getInstance();
			Calendar begin = Calendar.getInstance();

			begin.set(NumberUtils.getInt(temp[0]), NumberUtils.getInt(temp[1]), NumberUtils.getInt(temp[2]));
			ends.set(NumberUtils.getInt(temp2[0]), NumberUtils.getInt(temp2[1]), NumberUtils.getInt(temp2[2]));
			if (begin.compareTo(ends) < 0) {
				Map<String, Integer> map = new HashMap<String, Integer>(3);
				ends.add(1, -NumberUtils.getInt(temp[0]));
				ends.add(2, -NumberUtils.getInt(temp[1]));
				ends.add(5, -NumberUtils.getInt(temp[2]));
				map.put("YEAR", Integer.valueOf(ends.get(1)));
				map.put("MONTH", Integer.valueOf(ends.get(2) + 1));
				map.put("DAY", Integer.valueOf(ends.get(5)));
				return map;
			}
		}
		return null;
	}

	public static Date rollDay(Date d, int day) {
		Calendar cal = Calendar.getInstance();
	    cal.setTime(d);
	    cal.add(Calendar.DATE, day);
	    return cal.getTime();
	}
  
	public static Date rollMon(Date d, int mon) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(2, mon);
		return cal.getTime();
	}
	
	public static Date rollYear(Date d, int year) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(1, year);
		return cal.getTime();
	}
	
	public static Date rollDate(Date d, int year, int mon, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(1, year);
		cal.add(2, mon);
		cal.add(5, day);
		return cal.getTime();
	}

	public static String getNowTimeStr() {
		String str = Long.toString(System.currentTimeMillis() / 1000L);
		return str;
	}
	
	public static String getTimeStr(Date time) {
		long date = time.getTime();
		String str = Long.toString(date / 1000L);
		return str;
	}
	
	public static String rollMonth(String addtime, String time_limit) {
		Date t = rollDate(getDate(addtime), 0, NumberUtils.getInt(time_limit), 0);
		return String.valueOf(t.getTime() / 1000L);
	}

	public static String rollDay(String addtime, String time_limit_day) {
		Date t = rollDate(getDate(addtime), 0, 0, NumberUtils.getInt(time_limit_day));
		return String.valueOf(t.getTime() / 1000L);
	}

	public static Date getIntegralTime() {
	    Calendar cal = Calendar.getInstance();
	    cal.set(11, 0);
	    cal.set(13, 0);
	    cal.set(12, 0);
	    cal.set(14, 0);
	    return cal.getTime();
	}
	
	public static Date getLastIntegralTime() {
	    Calendar cal = Calendar.getInstance();
	    cal.set(11, 23);
	    cal.set(13, 59);
	    cal.set(12, 59);
	    cal.set(14, 0);
	    return cal.getTime();
	}
	
	public static Date getLastSecIntegralTime(Date d) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(d.getTime());
	    cal.set(11, 23);
	    cal.set(13, 59);
	    cal.set(12, 59);
	    cal.set(14, 0);
	    return cal.getTime();
	}

	public static Date getNexyDate(Date date) {
		date = rollDay(date, 1);
		return getDate1(dateStr3(date));
	}


	public static boolean valid(String str) {
		return Validate.IsDateTimeString(str);
	}
}
