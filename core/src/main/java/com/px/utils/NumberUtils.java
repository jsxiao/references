package com.px.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public final class NumberUtils {

	public static String currencyFormat(double d, int minDigits, int maxDigits){
		
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
        
        //currencyFormat.setMaximumFractionDigits(4);
        //currencyFormat.setMinimumIntegerDigits(6);
		
		return currencyFormat.format(d);
	}
	
	public static String currencyFormat(double d){
		return currencyFormat(d, 0, 0);
	}
	
	/**
	 * 数字格式化
	 * @param d 要格式化的数字
	 * @param format 格式化字符串
	 * @return
	 */
	public static double format(double d, String format) {
		DecimalFormat df = new DecimalFormat(format);
		String ds = df.format(d);
		return Double.parseDouble(ds);
	}

	/**
	 * 不带小数切增加千分位
	 * @param d
	 * @return
	 */
	public static String format7(double d){
		DecimalFormat df = new DecimalFormat("###,##0");
		String ds = df.format(d);
		return ds;
	}
	
	/**
	 * 保留两位小数增加千分位
	 * @param d
	 * @return
	 */
	public static String format8(double d){
		DecimalFormat df = new DecimalFormat("###,##0.00");
		String ds = df.format(d);
		return ds;
	}

	public static double format2(double d) {
		BigDecimal big = BigDecimal.valueOf(d);
		big = big.setScale(2, 6);
		return big.doubleValue();
	}

	public static double formatTo2(double d) {
		DecimalFormat df = new DecimalFormat("0.00");
		String ds = df.format(d);
		return Double.parseDouble(ds);
	}

	public static double formatup2(double d) {
		BigDecimal big = BigDecimal.valueOf(d);
		big = big.setScale(2, 4);
		return big.doubleValue();
	}

	public static double getDoubleFormat2Up(double d) {
		BigDecimal big = BigDecimal.valueOf(d);
		big = big.setScale(2, 0);
		return big.doubleValue();
	}

	public static String format2Str(double d) {
		DecimalFormat df = new DecimalFormat("0.00");
		String ds = df.format(d);
		return ds;
	}

	public static String format3Str(double d) {
		DecimalFormat df = new DecimalFormat("0.000");
		String ds = df.format(d);
		return ds;
	}

	public static double format4(double d) {
		DecimalFormat df = new DecimalFormat("0.0000");
		String ds = df.format(d);
		return Double.parseDouble(ds);
	}

	public static double format6(double d) {
		DecimalFormat df = new DecimalFormat("0.000000");
		String ds = df.format(d);
		return Double.parseDouble(ds);
	}

	public static double getDouble(String str) {
		if ((str == null) || (str.equals("")))
			return 0.0D;
		double ret = 0.0D;
		try {
			ret = Double.parseDouble(str);
		} catch (NumberFormatException e) {
			ret = 0.0D;
		}
		return format6(ret);
	}

	public static long getLong(String str) {
		if ((str == null) || (str.equals("")))
			return 0L;
		long ret = 0L;
		try {
			ret = Long.parseLong(str);
		} catch (NumberFormatException e) {
			ret = 0L;
		}
		return ret;
	}

	public static Long[] getLongs(String[] str) {
		if ((str == null) || (str.length < 1))
			return new Long[] { Long.valueOf(0L) };
		Long[] ret = new Long[str.length];
		for (int i = 0; i < str.length; i++) {
			ret[i] = Long.valueOf(getLong(str[i]));
		}
		return ret;
	}

	public static Double[] getDoubles(String[] str) {
		if ((str == null) || (str.length < 1))
			return new Double[] { Double.valueOf(0.0D) };
		Double[] ret = new Double[str.length];
		for (int i = 0; i < str.length; i++) {
			ret[i] = Double.valueOf(getDouble(str[i]));
		}
		return ret;
	}

	public static int[] getInts(String[] str) {
		if ((str == null) || (str.length < 1))
			return new int[1];
		int[] ret = new int[str.length];
		for (int i = 0; i < str.length; i++) {
			ret[i] = getInt(str[i]);
		}
		return ret;
	}

	public static int getInt(String str) {
		if ((str == null) || (str.equals("")))
			return 0;
		int ret = 0;
		try {
			ret = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			ret = 0;
		}
		return ret;
	}

	public static int compare(double x, double y) {
		BigDecimal val1 = new BigDecimal(x);
		BigDecimal val2 = new BigDecimal(y);
		return val1.compareTo(val2);
	}

	public static double ceil(double d, int len) {
		String str = Double.toString(d);
		int a = str.indexOf(".");
		if (a + 3 > str.length())
			a = str.length();
		else {
			a += 3;
		}
		str = str.substring(0, a);
		return Double.parseDouble(str);
	}

	public static double ceil(double d) {
		return ceil(d, 2);
	}

	public static long getRandom(int len) {
     double r = Math.random();
     for (int i = 0; i < len; i++) {
       r *= 10.0D;
     }
     long ret = (long) r;
     return ret;
   }

	public static double subtract(double num1, double num2) {
		BigDecimal numa = new BigDecimal(num1 * 100000.0D);
		BigDecimal numb = new BigDecimal(num2 * 100000.0D);
		double result = numa.subtract(numb).doubleValue() / 100000.0D;
		return result;
	}

	public static double add(double num1, double num2) {
		BigDecimal numa = new BigDecimal(num1 * 100000.0D);
		BigDecimal numb = new BigDecimal(num2 * 100000.0D);
		double result = numa.add(numb).doubleValue() / 100000.0D;
		return result;
	}

	public static double getDouble2(String numStr) {
		double num = getDouble(numStr);
		return format2(num);
	}
	
	/**
	 * null或0都是true
	 * @param number
	 * @return
	 */
	public static boolean isNull(Serializable number){
		
		if(number != null){
			if(number instanceof BigDecimal)
				return ((BigDecimal) number).doubleValue() == 0; 
			
			if(number instanceof Integer)
				return ((Integer) number).intValue() == 0;
			
			if(number instanceof Long)
				return ((Long) number).longValue() == 0;
			
			if(number instanceof Double)
				return ((Double) number).doubleValue() == 0;
		}
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println(format7(10000.0));
	}
}
