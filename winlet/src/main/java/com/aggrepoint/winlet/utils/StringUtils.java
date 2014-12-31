package com.aggrepoint.winlet.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Random;

/**
 * 字符串处理工具方法
 * 
 * @author JMYANG
 */
public class StringUtils {
	/**
	 * 字符串替换
	 * 
	 * @param str
	 *            String
	 * @param strReplace
	 *            String
	 * @param strWith
	 * @return String
	 */
	static public String replaceString(String str, String strReplace,
			String strWith) {
		StringBuffer sb = new StringBuffer();
		int iCurrent = 0;
		int iFound;
		int iReplaceLen = strReplace.length();

		if (str == null || strReplace == null || strWith == null)
			return str;
		if (str.equals("") || strReplace.equals(""))
			return str;

		while (true) {
			iFound = str.indexOf(strReplace, iCurrent);
			if (iFound == -1) {
				sb.append(str.substring(iCurrent));
				break;
			}

			if (iFound != iCurrent)
				sb.append(str.substring(iCurrent, iFound));
			sb.append(strWith);
			iCurrent = iFound + iReplaceLen;
		}

		return sb.toString();
	}

	/**
	 * 获取字符串在数据库中占据的字节长度
	 * 
	 * @param str
	 *            String 要处理的字符串
	 * @param n
	 *            int 每个非拉丁字符在数据库中占据的字节数
	 */
	public static int getDBLength(String str, int n) {
		if (str == null) {
			return 0;
		}

		char[] chars = str.toCharArray();
		int len = 0;

		for (int i = 0; i < chars.length; i++) {
			if (Character.UnicodeBlock.of(chars[i]) == Character.UnicodeBlock.BASIC_LATIN) {
				len++;
			} else {
				len += n;
			}
		}

		return len;
	}

	/**
	 * 将超出长度的字符截去。
	 * 
	 * @param str
	 *            String 要处理的字符串
	 * @param dbLen
	 *            int 长度
	 * @param n
	 *            int 每个非拉丁字符在数据库中占据的字节数
	 */
	public static String fixDBString(String str, int dbLen, int n) {
		if (str == null) {
			return "";
		}

		char[] chars = str.toCharArray();
		int len = 0;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < chars.length; i++) {
			if (Character.UnicodeBlock.of(chars[i]) == Character.UnicodeBlock.BASIC_LATIN) {
				len++;
			} else {
				len += n;
			}
			if (len > dbLen) {
				break;
			}
			sb.append(chars[i]);
		}

		return sb.toString();
	}

	/**
	 * 将超出长度的字符串截去。 处理后的字符串可以用来准备PreparedStatement。
	 * 
	 * @param str
	 *            String 要处理的字符串，可以为null
	 * @param dbLen
	 *            int 长度
	 */
	public static String fixLen(String str, int dbLen, int n) {
		if (str == null)
			return null;

		if (str.length() * n < dbLen)
			return str;

		char[] chars = str.toCharArray();
		int len = 0;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < chars.length; i++) {
			if (Character.UnicodeBlock.of(chars[i]) == Character.UnicodeBlock.BASIC_LATIN) {
				len++;
			} else {
				len += n;
			}
			if (len > dbLen) {
				break;
			}
			sb.append(chars[i]);
		}

		return sb.toString();
	}

	/**
	 * 获取字符串前len个字符。若字符串长度长于len，则后面用省略号代替。
	 * 
	 * @param str
	 * @param len
	 * @param c
	 *            省略号字符
	 * @return
	 */
	public static String getBrief(String str, int len, char c) {
		if (str == null)
			return "";
		if (str.length() < len)
			return str;
		return new StringBuffer(str.substring(0, len - 3)).append(c).append(c)
				.append(c).toString();
	}

	/**
	 * 获取字符串前len个字符。若字符串长度长于len，则后面用英文省略号代替。
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String getBrief(String str, int len) {
		return getBrief(str, len, '.');
	}

	/**
	 * 将Unicode的Byte Array转换为Java字符串
	 * 
	 * @param ba
	 *            byte[] Unicode的Byte Array
	 * @return String 转换结果
	 */
	public static String strFromUnicodeByteArray(byte[] ba, int len) {
		if (ba == null) {
			return "";
		}

		byte[] newBa = new byte[len];
		for (int i = 0; i < len; i++)
			newBa[i] = ba[i];

		StringBuffer sb = new StringBuffer();
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
				newBa));
		while (true) {
			try {
				char c = dis.readChar();
				sb.append(c);
			} catch (Exception e) {
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 将Java字符串转换为Unicode的Byte Array。
	 * 
	 * @param str
	 *            String 要转换的字符串
	 * @return byte[] 转换结果
	 */
	public static byte[] getUnicodeByteArray(String str) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			for (int i = 0; i < str.length(); i++) {
				dos.writeChar((int) str.charAt(i));
			}
		} catch (Exception e) {
		}

		return baos.toByteArray();
	}

	protected static void appendWMLUnicodeToStringBuffer(StringBuffer sb,
			byte[] ba) {
		int b;

		for (int i = 0; i < ba.length; i += 2) {
			sb.append("&#x");
			b = ba[i] >= 0 ? ba[i] : 256 + ba[i];
			if (b < 16)
				sb.append("0");
			sb.append(Integer.toHexString(b));
			b = ba[i + 1] >= 0 ? ba[i + 1] : 256 + ba[i + 1];
			if (b < 16)
				sb.append("0");
			sb.append(Integer.toHexString(b));
			sb.append(";");
		}
	}

	public static String getWMLUnicodeString(String str) {
		StringBuffer sb = new StringBuffer();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		char c;

		try {
			for (int i = 0; i < str.length(); i++) {
				c = str.charAt(i);

				if (Character.UnicodeBlock.of(c) != Character.UnicodeBlock.BASIC_LATIN) {
					// 不是基本的英文字符
					dos.writeChar((int) c);
				} else { // 基本英文字符
					if (baos.size() != 0) { // 前面已经有非英文字符出现
						appendWMLUnicodeToStringBuffer(sb, baos.toByteArray());
						baos.reset();
					}

					sb.append(c);
				}
			}

			if (baos.size() != 0)
				appendWMLUnicodeToStringBuffer(sb, baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * 将指定的字符串str按charCount个字符为一段进行分段，取出第part段。 part从1开始 如果指定的分段不存在，则返回null。
	 */
	static public String getSlice(String str, int charCount, int part) {
		if (str == null)
			return null;

		int len = str.length();
		int iFrom = charCount * (part - 1);
		if (iFrom >= len)
			return null;
		int iTo = iFrom + charCount;
		if (iTo > len)
			iTo = len;

		return str.substring(iFrom, iTo);
	}

	/**
	 * 将Exception的Stack Trace转为字符串
	 */
	static public String getStackTrace(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);
		ps.flush();
		try {
			return baos.toString("UTF-8");
		} catch (UnsupportedEncodingException ee) {
			return baos.toString();
		}
	}

	final static byte[] RANDOM_KEY_CHARACTER_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.getBytes();

	final static Random m_rn = new Random();

	public static String getRandomKey(int keyLength) {
		byte[] result = new byte[keyLength];
		for (int i = 0; i < keyLength; i++)
			result[i] = RANDOM_KEY_CHARACTER_SET[m_rn
					.nextInt(RANDOM_KEY_CHARACTER_SET.length)];
		return new String(result);
	}

	public static String toHTMLString(String org) {
		StringBuffer result = new StringBuffer(org.length());
		char[] chars = org.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '\"')
				result.append("&quot;");
			else if (chars[i] == '<')
				result.append("&lt;");
			else if (chars[i] == '>')
				result.append("&gt;");
			else if (chars[i] == '&')
				result.append("&amp;");
			else
				result.append(chars[i]);
		}
		return result.toString();
	}

	public static String toHTML(String org, boolean inputValue) {
		StringBuffer result = new StringBuffer(org.length());
		char[] chars = org.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '\"')
				result.append("&quot;");
			else if (chars[i] == '<')
				result.append("&lt;");
			else if (chars[i] == '>')
				result.append("&gt;");
			else if (chars[i] == '&')
				result.append("&amp;");
			else if (chars[i] == '\r') {
				if (inputValue)
					result.append(chars[i]);
				else {
					result.append("<br/>");
					if (i + 1 < chars.length && chars[i + 1] == '\n')
						i++;
				}
			} else if (chars[i] == '\n') {
				if (inputValue)
					result.append(chars[i]);
				else
					result.append("<br/>");
			} else if (chars[i] == ' ')
				if (inputValue)
					result.append(chars[i]);
				else
					result.append("&nbsp;");
			else
				result.append(chars[i]);
		}
		return result.toString();
	}

	public static String toHTML(String org) {
		return toHTML(org, false);
	}

	public static String toWML(String org) {
		StringBuffer result = new StringBuffer(org.length());
		char[] chars = org.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '\"')
				result.append("&quot;");
			else if (chars[i] == '<')
				result.append("&lt;");
			else if (chars[i] == '>')
				result.append("&gt;");
			else if (chars[i] == '&')
				result.append("&amp;");
			else if (chars[i] == '\r') {
				result.append("<br/>");
				if (i + 1 < chars.length && chars[i + 1] == '\n')
					i++;
			} else if (chars[i] == '\n')
				result.append("<br/>");
			else if (chars[i] == ' ')
				result.append("&#160;");
			else
				result.append(chars[i]);
		}
		return result.toString();
	}

	public static String toXMLString(String org) {
		if (org == null)
			return "";

		StringBuffer result = new StringBuffer(org.length());
		char[] chars = org.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '<')
				result.append("&lt;");
			else if (chars[i] == '>')
				result.append("&gt;");
			else if (chars[i] == '&')
				result.append("&amp;");
			else if (chars[i] == '\r')
				result.append("&#13;");
			else if (chars[i] == '\n')
				result.append("&#10;");
			else if (chars[i] == '\"')
				result.append("&#34;");
			else
				result.append(chars[i]);
		}
		return result.toString();
	}

	public static String toJson(String org) {
		StringBuffer result = new StringBuffer(org.length());
		char[] chars = org.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '\"')
				result.append("&#34");
			else if (chars[i] == '\'')
				result.append("&#39");
			else if (chars[i] == '\\')
				result.append("&#92");
			else if (chars[i] == '\r') {
				result.append("\\r");
			} else if (chars[i] == '\n')
				result.append("\\n");
			else
				result.append(chars[i]);
		}
		return result.toString();
	}

	public static String fixJson(String org) {
		if (org == null)
			return org;

		org = org.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&quot;", "\\\\\"").replaceAll("&nbsp;", " ")
				.replaceAll("&amp;", "&");
		return org;
	}

	/**
	 * 将以yyyy-MM-dd形式的字符串转换为Calendar对象
	 * 
	 * @param time
	 *            时间字符串
	 * @param sep
	 *            分隔符号
	 * @param isStart
	 *            为true表示时、分、秒、毫秒都取0，为false表示取最后一刻
	 */
	public static Calendar getCalendar(String time, char sep, boolean isStart) {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.HOUR_OF_DAY, isStart ? 0 : 23);
		cal.set(Calendar.MINUTE, isStart ? 0 : 59);
		cal.set(Calendar.SECOND, isStart ? 0 : 59);
		cal.set(Calendar.MILLISECOND, isStart ? 0 : 999);

		try {
			int idx = time.indexOf(sep);

			if (idx != -1) {
				cal.set(Calendar.YEAR, Integer.parseInt(time.substring(0, idx)));
				time = time.substring(idx + 1);
				idx = time.indexOf(sep);
				if (idx != -1) {
					cal.set(Calendar.MONTH,
							Integer.parseInt(time.substring(0, idx)) - 1);
					cal.set(Calendar.DAY_OF_MONTH,
							Integer.parseInt(time.substring(idx + 1)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cal;
	}

	public static String toDbMatch(String str) {
		if (str == null)
			str = "";
		return "%" + str + "%";
	}

	public static String toDbMatch(String str, String escape) {
		if (escape == null)
			return toDbMatch(str);

		if (str == null)
			str = "";

		str = str
				.replaceAll(escape.equals("\\") ? escape + escape : escape,
						escape + escape).replaceAll("%", escape + "%")
				.replaceAll("_", escape + "_");
		return "%" + str + "%";
	}

	public static String removeDbMatch(String str) {
		if (str == null)
			return "";

		if (str.startsWith("%"))
			str = str.substring(1);

		if (str.endsWith("%"))
			str = str.substring(0, str.length() - 1);

		return str;
	}

	public static String wrap(String str, int width) {
		if (str == null || width <= 0 || str.length() <= width)
			return str;

		char[] chars = str.toCharArray();
		width = width * 2;
		int c = 0;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if (Character.UnicodeBlock.of(chars[i]) == Character.UnicodeBlock.BASIC_LATIN)
				c++;
			else
				c += 2;
			sb.append(chars[i]);
			if (c >= width) {
				c = 0;
				sb.append("<br/>");
			}
		}

		return sb.toString();
	}

	public static class TruncateResult {
		public String str;
		public boolean bTruncated;

		public TruncateResult(String str, boolean truncated) {
			this.str = str;
			bTruncated = truncated;
		}
	}

	public static TruncateResult truncate(String str, int width) {
		if (str == null || width <= 0 || str.length() <= width)
			return new TruncateResult(str, false);

		char[] chars = str.toCharArray();
		width = width * 2 - 3;
		int c = 0;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if (Character.UnicodeBlock.of(chars[i]) == Character.UnicodeBlock.BASIC_LATIN)
				c++;
			else
				c += 2;
			sb.append(chars[i]);
			if (c >= width) {
				sb.append("...");
				return new TruncateResult(sb.toString(), true);
			}
		}
		return new TruncateResult(sb.toString(), false);
	}

	public static boolean equals(String str1, String str2) {
		if (str1 == null && str2 == null)
			return true;
		if (str1 == null || str2 == null)
			return false;
		return str1.equals(str2);
	}

	public static String capitalize(String str) {
		if (str == null || str.equals(""))
			return str;
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String appendUrl(String str1, String str2) {
		if (str1 == null || str1.equals(""))
			return str2;
		if (str2 == null || str2.equals(""))
			return str1;

		if (str1.endsWith("/") && str2.startsWith("/")) {
			return str1 + str2.substring(1);
		}

		if (!str1.endsWith("/") && !str2.startsWith("/"))
			return str1 + "/" + str2.substring(1);

		return str1 + str2;
	}

	public static int countMatches(String main, String sub) {
		if (main == null || sub == null || main.length() == 0
				|| sub.length() == 0)
			return 0;

		int idx = 0;
		int count = 0;
		int len = sub.length();
		idx = main.indexOf(sub, idx);
		while (idx >= 0) {
			count++;
			idx += len;
			idx = main.indexOf(sub, idx);
		}

		return count;
	}
}
