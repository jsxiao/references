package com.ec.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.collect.Maps;

/**
 * 
 * @author jason
 *
 */
public final class DateUtils {

	public static final String PATTERN_YYYYMMDD = "yyyy-MM-dd";
	public static final String PATTERN_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	
	private static final Object lockObj = new Object();
	private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = Maps.newHashMap();
	
	public static SimpleDateFormat get(String pattern){
		ThreadLocal<SimpleDateFormat> sdfThread = sdfMap.get(pattern);
		if(sdfThread == null){
			synchronized (lockObj) {
				sdfThread = sdfMap.get(pattern);
				if(sdfThread == null){
					sdfThread = new ThreadLocal<SimpleDateFormat>();
					sdfThread.set(new SimpleDateFormat(pattern));
					/*sdfThread = new ThreadLocal<SimpleDateFormat>(){
						@Override
						protected SimpleDateFormat initialValue() {
							return new SimpleDateFormat(pattern);
						}
					};*/
					sdfMap.put(pattern, sdfThread);
				}
			}
		}
		return sdfThread.get();
	}
	
	/**
	 * 返回当前时间
	 * @param args
	 * @return
	 */
	public static Date currentDate(){
		return new java.util.Date();
	}
	
	public static int diffWithMinute(Date arg1, Date arg2){
		Calendar cal = getCal();
		//cal.
		return 0;
	}
	
	public static Calendar getCal(){
		return Calendar.getInstance();
	}
	
	
	public static void main(String[] args) {
		//org.apache.commons.lang3.time.DateUtils.
	}
}
