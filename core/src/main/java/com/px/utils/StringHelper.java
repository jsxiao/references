package com.px.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.px.core.utils.StringFormat;

public final class StringHelper {

	private static final Logger logger = LoggerFactory.getLogger(StringHelper.class);
	
	private static Pattern emailRegex = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private static Pattern integerRegex = Pattern.compile("\\d*");
	private static Pattern numberRegex = Pattern.compile("\\d*(.\\d*)?");
	
	
	public static String isNull(String arg) {
		if(!StringUtils.hasText(arg))
			return "";
		
		else 
			return arg;
	}

	public static boolean isEmail(String email) {
		return emailRegex.matcher(isNull(email)).matches();
	}

	public static boolean isMobile(String mobiles) {
		Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isCard(String cardId) {
		cardId = isNull(cardId);

		Pattern isIDCard1 = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");

		Pattern isIDCard2 = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
		Matcher matcher1 = isIDCard1.matcher(cardId);
		Matcher matcher2 = isIDCard2.matcher(cardId);
		boolean isMatched = (matcher1.matches()) || (matcher2.matches());
		return isMatched;
	}

	public static boolean isInteger(String str) {
		if (isEmpty(str)) {
			return false;
		}
		return integerRegex.matcher(str).matches();
	}

	public static boolean isNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}
		return numberRegex.matcher(str).matches();
	}

	public static boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}

	public static String firstCharUpperCase(String s) {
		return StringUtils.capitalize(s);
	}

	public static String hideChar(String str, int len) {
		if (str == null)
			return null;
		char[] chars = str.toCharArray();
		for (int i = 1; i > chars.length - 1; i++) {
			if (i < len) {
				chars[i] = '*';
			}
		}
		str = new String(chars);
		return str;
	}

	public static String hideChar2(String str) {
		if (str == null)
			return null;
		char[] chars = str.toCharArray();
		int a = (int) Math.floor(chars.length / 2);
		for (int i = 0; i < a + 2; i++) {
			if ((i >= a - 2) && (i < a + 2)) {
				chars[i] = '*';
			}
		}
		str = new String(chars);
		return str;
	}

	public static String hideLastChar(String str, int len) {
		if (str == null)
			return null;
		char[] chars = str.toCharArray();
		if (str.length() <= len) {
			for (int i = 0; i < chars.length; i++)
				chars[i] = '*';
		} else {
			for (int i = chars.length - 1; i > chars.length - len - 1; i--) {
				chars[i] = '*';
			}
		}
		str = new String(chars);
		return str;
	}

	public static String format(String str, int len) {
		if (str == null)
			return "-";
		if (str.length() <= len) {
			int pushlen = len - str.length();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < pushlen; i++) {
				sb.append("0");
			}
			sb.append(str);
			str = sb.toString();
		} else {
			String newStr = str.substring(0, len);
			str = newStr;
		}
		return str;
	}

	public static String contact(Object[] args) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
			if (i < args.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public static boolean isInSplit(String s, String type) {
		if (isNull(s).equals("")) {
			return false;
		}
		List<String> list = Arrays.asList(s.split(","));
		if (list.contains(type)) {
			return true;
		}
		return false;
	}

	public static boolean isBlank(String str) {
		return isNull(str).equals("");
	}

	public static String array2Str(Object[] arr) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			s.append(arr[i]);
			if (i < arr.length - 1) {
				s.append(",");
			}
		}
		return s.toString();
	}

	public static String array2Str(int[] arr) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			s.append(arr[i]);
			if (i < arr.length - 1) {
				s.append(",");
			}
		}
		return s.toString();
	}

	public static boolean checkUsername(String username) {
		Pattern p = Pattern.compile("^(?![0-9]+$)[0-9A-Za-zΑ-￥]{4,15}$");
		Matcher m = p.matcher(username);
		return m.matches();
	}

	public static boolean checkwd(String pwd) {
		String regEx = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z~!@#$%^&*()]{8,16}$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(pwd);
		boolean rs = mat.find();
		return rs;
	}

	public static boolean pwdContainStr(String pwd) {
		Pattern p = Pattern.compile("[a-z|A-Z]");
		Matcher m = p.matcher(pwd);
		return m.find();
	}

	public static boolean pwdContainNum(String pwd) {
		Pattern p = Pattern.compile("[0-9]");
		Matcher m = p.matcher(pwd);
		return m.find();
	}

	public static boolean checaAdminPwd(String pwd) {
		String regEx = "^(?![0-9A-Za-z]+$)[0-9A-Za-z~!@#$%^&*()]{10,}$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(pwd);
		boolean rs = mat.find();
		return rs;
	}

	public static String gbk2Utf(String gbk) {
		
		char[] c = gbk.toCharArray();
		byte[] fullByte = new byte[3 * c.length];
		for (int i = 0; i < c.length; i++) {
			String binary = Integer.toBinaryString(c[i]);
			StringBuffer sb = new StringBuffer();
			int len = 16 - binary.length();

			for (int j = 0; j < len; j++) {
				sb.append("0");
			}
			sb.append(binary);

			sb.insert(0, "1110");
			sb.insert(8, "10");
			sb.insert(16, "10");
			fullByte[(i * 3)] = Integer.valueOf(sb.substring(0, 8), 2).byteValue();
			fullByte[(i * 3 + 1)] = Integer.valueOf(sb.substring(8, 16), 2).byteValue();
			fullByte[(i * 3 + 2)] = Integer.valueOf(sb.substring(16, 24), 2).byteValue();
		}

		try{
			return new String(fullByte, "UTF-8");
		}catch(UnsupportedEncodingException e){
			logger.warn("{} to utf8 is error", gbk);
		}
		return "";
	}

	public static boolean checkDateString(String dateStr) {
		String eL = "[1-9]{1}[0-9]{3}-[0-9]{2}-[0-9]{2}";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(dateStr);
		return m.matches();
	}

	public static String getGbk(String str)  {
		try{
			return new String(str.getBytes("UTF-8"), "GB2312");
		}catch(UnsupportedEncodingException e){
			logger.warn("{} to gbk is error", str);
		}
		return "";
	}

	public static String getEmailLoginUrl(String email) {
		String url = email.split("@")[1];
		return "http://mail." + url;
	}

	/**
	 * 转为大写
	 * @param original
	 * @return e.g: 壹貳叁肆伍陆柒
	 */
	public static String upNumb(double original) {
		return StringFormat.getCnString(original);
	}

	/**
	 * 将传入的字符串解码(UTF-8)
	 * @param sStr
	 * @return
	 */
	public static String UrlDecoder(String sStr) {
		return StringFormat.UrlDecode(sStr);
	}

	/**
	 * 从身份证号中获取生日
	 * @param card
	 * @return yyyyMMdd
	 */
	public static String getBirthdayFromIdCard(String card) {
		if ((card == null) || ("".equals(card))) {
			return "";
		}
		String birthday = card.substring(6, 14);
		return birthday;
	}

	public static String copyCharInSpecifyNum(String ch, int num) {
		StringBuffer copy = new StringBuffer();
		for (int i = 0; i < num; i++) {
			copy.append(ch);
		}
		return copy.toString();
	}

	/**
	 * 从身份证号中获取性别
	 * @param cardId
	 * @return 
	 */
	public static int getGenderFromIdCard(String cardId) {
		int sexNum = 0;
		if (cardId.length() == 15)
			sexNum = cardId.charAt(14);
		else {
			sexNum = cardId.charAt(16);
		}
		if (sexNum % 2 == 1) {
			return 1;
		}
		return 2;
	}
}
