package com.px.core.utils;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * 字符处理函数 //
 */
public class StringFormat
{

	// HTMLEncode 保持字符的格式
	/**
	 * HTMLEncode 保持字符的格式
	 * 
	 * @param FString
	 *            要格式化的字符串
	 * 
	 */
	public static String HTMLEncode(String FString)
	{
		if (FString != null)
		{
			// FString = FString.replace("", "&gt;");
			FString = FString.replace("<", "&lt;");
			FString = FString.replace(" ", "&nbsp;");
			FString = FString.replace("\t", "&nbsp;");
			FString = FString.replace("\"", "&quot;");
			FString = FString.replace("'", "&#39;");
			FString = FString.replace("\n", "<br />");
			// FString = FString.replace("\r", "<br />");

		}

		return FString;
	}

	/**
	 * 类似.net自带的HtmlEncode编码函数一样
	 * 
	 * @param s
	 *            需要转换的字符串
	 * @return
	 */
	public static String HtmlEncode(Object s)
	{
		return HtmlEncode(Converts.objToStr(s));
	}

	/**
	 * 类似.net自带的HtmlEncode编码函数一样
	 * 
	 * @param s
	 *            需要转换的字符串
	 * @return
	 */
	public static String HtmlEncode(String s)
	{
		if (s == null)
		{
			s = "";
		}
		int j = s.length();

		StringBuffer stringbuffer = new StringBuffer((int) (j * 1.5));
		for (int i = 0; i < j; i++)
		{
			char c = s.charAt(i);
			switch (c)
			{
			case 60:
				stringbuffer.append("&lt;");
				break;
			case 62:
				stringbuffer.append("&gt;");
				break;
			case 38:
				stringbuffer.append("&amp;");
				break;
			case 34:
				stringbuffer.append("&quot;");
				break;
			case 169:
				stringbuffer.append("&copy;");
				break;
			case 174:
				stringbuffer.append("&reg;");
				break;
			case 165:
				stringbuffer.append("&yen;");
				break;
			case 8364:
				stringbuffer.append("&euro;");
				break;
			case 8482:
				stringbuffer.append("&#153;");
				break;
			// case 13:
			// if (i < j - 1 && s.charAt(i + 1) == 10)
			// {
			// stringbuffer.append("<br>");
			// i++;
			// }
			// break;
			// 这里隐藏是因为这个在xml中出错
			// case 32:
			// if (i < j - 1 && s.charAt(i + 1) == ' ')
			// {
			// stringbuffer.append(" &nbsp;");
			// i++;
			// break;
			// }
			default:
				stringbuffer.append(c);
				break;
			}
		}
		return new String(stringbuffer.toString());
	}

	// HTMLEncode 保持字符的格式
	/**
	 * HTMLEncode 保持字符的格式
	 * 
	 * @param FString
	 *            要格式化的字符串
	 * 
	 */

	public static String HTMLDecode(String FString)
	{

		FString = FString.replace("&gt;", "");
		FString = FString.replace("&lt;", "<");
		FString = FString.replace("&nbsp;", " ");
		FString = FString.replace("&nbsp;", "\t");
		FString = FString.replace("&quot;", "\"");
		FString = FString.replace("&#39;", "'");
		FString = FString.replace("", "\n");
		FString = FString.replace("</p><p>", "\n");
		FString = FString.replace("<br />", "\n");

		return FString;
	}

	// Escape
	// private static String[] hex = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A",
	// "1B", "1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A",
	// "3B", "3C", "3D", "3E", "3F", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5A",
	// "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7A",
	// "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A",
	// "9B", "9C", "9D", "9E", "9F", "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA",
	// "BB", "BC", "BD", "BE", "BF", "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA",
	// "DB", "DC", "DD", "DE", "DF", "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA",
	// "FB", "FC", "FD", "FE", "FF" };
	// private static byte[] val = { 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
	// 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x3F,
	// 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
	// 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
	// 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
	// 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
	// 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
	// 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
	// 0x3F, 0x3F, 0x3F, 0x3F, 0x3F };

	// jsEscape JavaScrip Escape 函数编码
	/**
	 * JavaScrip Escape 函数编码
	 * 
	 * @param s
	 *            需要转换的字符串
	 */
	public static String jsEscape(String s)
	{
		if (s == null)
		{
			return "";
		}
		s = s.trim();
		final int URL_XALPHAS = 1;
		final int URL_XPALPHAS = 2;
		final int URL_PATH = 4;

		int mask = URL_XALPHAS | URL_XPALPHAS | URL_PATH;
		StringBuffer sb = null;

		for (int k = 0, L = s.length(); k != L; ++k)
		{
			int c = s.charAt(k);

			if (mask != 0 && (c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '@' || c == '*' || c == '_' || c == '-' || c == '.' || 0 != (mask & URL_PATH) && (c == '/' || c == '+')))
			{
				if (sb != null)
				{
					sb.append((char) c);
				}
			}
			else
			{
				if (sb == null)
				{
					sb = new StringBuffer(L + 3);
					sb.append(s);
					sb.setLength(k);
				}

				int hexSize;

				if (c < 256)
				{
					if (c == ' ' && mask == URL_XPALPHAS)
					{
						sb.append('+');

						continue;
					}

					sb.append('%');
					hexSize = 2;
				}
				else
				{
					sb.append('%');
					sb.append('u');
					hexSize = 4;
				}

				// append hexadecimal form of c left-padded with 0
				for (int shift = (hexSize - 1) * 4; shift >= 0; shift -= 4)
				{
					int digit = 0xf & c >> shift;
					int hc = digit < 10 ? '0' + digit : 'A' - 10 + digit;
					sb.append((char) hc);
				}
			}
		}

		return sb == null ? s : sb.toString();

	}

	// jsUnescape JavaScrip Unescape 函数解码
	/**
	 * JavaScrip Unescape 函数解码
	 * 
	 * @param s
	 *            需要转换的字符串
	 */
	public static String jsUnescape(String s)
	{
		String ret = s;
		int firstEscapePos = s.indexOf('%');

		if (firstEscapePos >= 0)
		{
			int L = s.length();
			char[] buf = s.toCharArray();
			int destination = firstEscapePos;

			for (int k = firstEscapePos; k != L;)
			{
				char c = buf[k];
				++k;

				if (c == '%' && k != L)
				{
					int end;
					int start;

					if (buf[k] == 'u')
					{
						start = k + 1;
						end = k + 5;
					}
					else
					{
						start = k;
						end = k + 2;
					}

					if (end <= L)
					{
						int x = 0;

						for (int i = start; i != end; ++i)
						{
							x = xDigitToInt(buf[i], x);
						}

						if (x >= 0)
						{
							c = (char) x;
							k = end;
						}
					}
				}

				buf[destination] = c;
				++destination;
			}

			ret = new String(buf, 0, destination);
		}

		return ret;

	}

	/**
	 * If character <tt>c</tt> is a hexadecimal digit, return <tt>accumulator</tt> * 16 plus corresponding number. Otherise return -1.
	 */
	private static int xDigitToInt(int c, int accumulator)
	{
		check:
		{
			// Use 0..9 < A..Z < a..z
			if (c <= '9')
			{
				c -= '0';

				if (0 <= c)
				{
					break check;
				}
			}
			else if (c <= 'F')
			{
				if ('A' <= c)
				{
					c -= 'A' - 10;

					break check;
				}
			}
			else if (c <= 'f')
			{
				if ('a' <= c)
				{
					c -= 'a' - 10;

					break check;
				}
			}

			return -1;
		}

		return accumulator << 4 | c;
	}

	// Base64Encode
	/**
	 * Base64编码
	 * 
	 * @param code
	 *            需要编码的字符
	 * @param encoding
	 *            需要编码的类型
	 */
	public static String Base64Encode(String code)
	{

		return Base64Encode(code, Encoding.UTF8);
	}

	/**
	 * Base64编码
	 * 
	 * @param code
	 *            需要编码的字符
	 * @param encoding
	 *            需要编码的类型
	 */
	public static String Base64Encode(String code, Encoding encoding)
	{
		// Base64 base64 = new Base64();

		try
		{

			String enbytes = Base64.encode(code.getBytes(encoding.toString()));
			return enbytes;
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return "";
		}

	}

	// Base64Decode
	/**
	 * Base64解码
	 * 
	 * @param code
	 *            需要编码的字符
	 * @param encoding
	 *            需要编码的类型
	 */
	public static String Base64Decode(String code)
	{

		return Base64Decode(code, Encoding.UTF8);
	}

	/**
	 * Base64解码
	 * 
	 * @param code
	 *            需要编码的字符
	 * @param encoding
	 *            需要编码的类型
	 */
	public static String Base64Decode(String code, Encoding encoding)
	{
		// Base64 base64 = new Base64();

		try
		{
			byte[] debytes = Base64.decode(code);
			return new String(debytes, encoding.toString());
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return "";
		}

	}

	// StringToAsciit 字符到ASCII码
	/**
	 * 字符到ASCII码
	 * 
	 * @param strSource
	 *            需要转换的字符串
	 */
	public static String StringToAsciit(String strSource)
	{

		StringBuffer sbStr = new StringBuffer(1024);
		char[] strSql = strSource.toCharArray();

		for (char s : strSql)
		{
			try
			{
				sbStr.append((int) s);
				sbStr.append(",");

				// 将当前系统挂起1毫秒
				// System.Threading.Thread.Sleep(1);
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		return sbStr.toString();
	}

	// AsciitoString ASCII码到字符
	/**
	 * ASCII码到字符
	 * 
	 * @param strSource
	 *            需要转换的字符串
	 */
	public static String AsciitoString(String strSource)
	{
		String[] strSql = strSource.trim().split(",");

		StringBuffer sbStr = new StringBuffer(1024);
		for (String s : strSql)
		{
			try
			{
				if (!s.equals(""))
				{
					sbStr.append((char) Integer.parseInt(s));
					// sbStr.append(",");

					// 将当前系统挂起1毫秒
					// System.Threading.Thread.Sleep(1);
				}
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		return sbStr.toString();
	}

	// JsEncode 转化为JS的格式
	/**
	 * 转化为JS的格式
	 * 
	 * @param FString
	 *            要格式化的字符串
	 */

	public static String JsEncode(String FString)
	{
		if (FString == null)
		{
			return "";
		}
		FString = FString.replace("\\", "\\\\");
		FString = FString.replace("\"", "\\\"");
		FString = FString.replace("\n", "\\n");
		FString = FString.replace("\r", "\\r");
		FString = FString.replace("/", "\\/");
		return FString.trim();
	}

	public static String JsEncode(Object FString)
	{
		return JsEncode(Converts.objToStr(FString));
	}

	// 去除html标记
	/**
	 * 去除html标记
	 * 
	 * @param strSql
	 *            原始字符串
	 * 
	 */
	public static String nohtml(String strSql)
	{
		if (strSql.trim().length() == 0)
		{
			return "";
		}
		// (\<.[^\<]*\>)
		strSql = strSql.replace("(?i)(\\b(?=\\w).[^<]*\\b(?!\\w))", "");
		// (\<\/[^\<]*\>)
		strSql = strSql.replace("(?i)(\\b(?=\\w)\\/[^<]*\\b(?!\\w))", "");

		strSql = strSql.replace("\"", "");
		strSql = strSql.replace("'", "");
		return strSql;
	}

	// 判断字符串的长度
	/**
	 * 判断字符串的长度
	 * 
	 * @param strSql
	 *            原始字符串
	 * @param length
	 *            截取字符的长度 *
	 * @param EndPoint
	 *            是否添加..
	 */
	public static String LeftTrue(String strSql, int length, boolean EndPoint)
	{
		if (strSql == null)
		{
			return "";
		}
		String StringToSub = strSql.trim();
		// Regex regex = new Regex("[\u4e00-\u9fa5]+", RegexOptions.Compiled);
		char[] StringChar = StringToSub.toCharArray();
		StringBuffer sb = new StringBuffer(1024);
		int nlength = 0;
		boolean isCut = false;
		for (char element : StringChar)
		{
			if (String.valueOf(element).matches("(?i)[\u4e00-\u9fa5]+"))
			{
				sb.append(element);
				nlength += 2;
			}
			else
			{
				sb.append(element);
				nlength = nlength + 1;
			}

			if (nlength > length)
			{
				isCut = true;
				break;
			}
		}
		if (isCut)
		{
			if (EndPoint)
			{
				return sb.toString() + "..";
			}
			else
			{
				return sb.toString();
			}
		}
		else
		{
			return sb.toString();
		}

	}

	// Unicode2UTF8
	/**
	 * Unicode转UTF8
	 * 
	 * @param s
	 *            需要转换的字符
	 */
	public static String Unicode2UTF8(String theString)
	{

		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);

		for (int x = 0; x < len;)
		{
			aChar = theString.charAt(x++);
			if (aChar == '\\')
			{
				aChar = theString.charAt(x++);
				if (aChar == 'u')
				{
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++)
					{
						aChar = theString.charAt(x++);
						switch (aChar)
						{
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				}
				else
				{
					if (aChar == 't')
					{
						aChar = '\t';
					}
					else if (aChar == 'r')
					{
						aChar = '\r';
					}
					else if (aChar == 'n')
					{
						aChar = '\n';
					}
					else if (aChar == 'f')
					{
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			}
			else
			{
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();

	}

	// 返回字符串真实长度, 1个汉字长度为2
	/**
	 * 返回字符串真实长度, 1个汉字长度为2
	 * 
	 * @param strSql
	 *            需要计算的字符
	 */
	public static int GetStringlength(String strSql)
	{
		return strSql.getBytes().length;
	}

	// 判断字符是否包含字符数组中的字符
	/**
	 * 判断字符是否包含字符数组中的字符
	 * 
	 * @param strSql
	 *            要检查的字符串
	 * @param Stringarray
	 *            字符数组
	 * @param strsplit
	 *            字符数字分割字符
	 */
	public static boolean IsCompriseStr(String strSql, String Stringarray, String strsplit)
	{
		if (Stringarray == null || Stringarray.length() == 0)
		{
			return false;
		}

		strSql = strSql.toLowerCase();
		String[] StringArray = SplitString(Stringarray.toLowerCase(), strsplit);
		for (String element : StringArray)
		{
			// String t1 = strSql;
			// String t2 = StringArray[i];
			if (strSql.indexOf(element) > -1)
			{
				return true;
			}
		}
		return false;
	}

	// 判断指定字符串在指定字符串数组中的位置

	// 判断指定字符串在指定字符串数组中的位置
	/**
	 * 判断指定字符串在指定字符串数组中的位置, 如不存在则返回-1
	 * 
	 * @param strSearch
	 *            字符串
	 * @param StringArray
	 *            字符串数组
	 * @param caseInsensetive
	 *            是否不区分大小写, true为不区分, false为区分
	 */
	public static int GetInArrayID(String strSearch, String[] StringArray, boolean caseInsensetive)
	{
		for (int i = 0; i < StringArray.length; i++)
		{
			if (caseInsensetive)
			{
				if (strSearch.toLowerCase().equals(StringArray[i].toLowerCase()))
				{
					return i;
				}
			}
			else
			{
				if (strSearch.equals(StringArray[i]))
				{
					return i;
				}
			}

		}
		return -1;
	}

	// 判断指定字符串在指定字符串数组中的位置
	/**
	 * 判断指定字符串在指定字符串数组中的位置, 如不存在则返回-1
	 * 
	 * @param strSearch
	 *            字符串
	 * @param StringArray
	 *            字符串数组
	 */
	public static int GetInArrayID(String strSearch, String[] StringArray)
	{
		return GetInArrayID(strSearch, StringArray, true);
	}

	// 判断指定字符串是否属于指定字符串数组中的一个元素

	// 判断指定字符串是否属于指定字符串数组中的一个元素
	/**
	 * 判断指定字符串是否属于指定字符串数组中的一个元素
	 * 
	 * @param strSearch
	 *            字符串
	 * @param StringArray
	 *            字符串数组
	 * @param caseInsensetive
	 *            是否不区分大小写, true为不区分, false为区分
	 */
	public static boolean InArray(String strSearch, String[] StringArray, boolean caseInsensetive)
	{
		return GetInArrayID(strSearch, StringArray, caseInsensetive) >= 0;
	}

	// 判断指定字符串是否属于指定字符串数组中的一个元素
	/**
	 * 判断指定字符串是否属于指定字符串数组中的一个元素
	 * 
	 * @param strSql
	 *            字符串
	 * @param Stringarray
	 *            字符串数组
	 * 
	 */
	public static boolean InArray(String strSql, String[] Stringarray)
	{
		return InArray(strSql, Stringarray, false);
	}

	// 判断指定字符串是否属于指定字符串数组中的一个元素
	/**
	 * 判断指定字符串是否属于指定字符串数组中的一个元素
	 * 
	 * @param strSql
	 *            字符串
	 * @param Stringarray
	 *            内部以逗号分割单词的字符串
	 */
	public static boolean InArray(String strSql, String Stringarray)
	{
		return InArray(strSql, SplitString(Stringarray, ","), false);
	}

	// 判断指定字符串是否属于指定字符串数组中的一个元素
	/**
	 * 判断指定字符串是否属于指定字符串数组中的一个元素
	 * 
	 * @param strSql
	 *            字符串
	 * @param Stringarray
	 *            内部以逗号分割单词的字符串
	 * @param strsplit
	 *            分割字符串 判断结果
	 */
	public static boolean InArray(String strSql, String Stringarray, String strsplit)
	{
		return InArray(strSql, SplitString(Stringarray, strsplit), false);
	}

	// 判断指定字符串是否属于指定字符串数组中的一个元素
	/**
	 * 判断指定字符串是否属于指定字符串数组中的一个元素
	 * 
	 * @param strSql
	 *            字符串
	 * @param Stringarray
	 *            内部以逗号分割单词的字符串
	 * @param strsplit
	 *            分割字符串
	 * @param caseInsensetive
	 *            是否不区分大小写, true为不区分, false为区分
	 */
	public static boolean InArray(String strSql, String Stringarray, String strsplit, boolean caseInsensetive)
	{
		return InArray(strSql, SplitString(Stringarray, strsplit), caseInsensetive);
	}

	// 分割字符串
	/**
	 * 分割字符串
	 * 
	 * @param strContent
	 *            字符内容
	 * @param strSplit
	 *            分隔字符，可以是正则表达式
	 */
	public static String[] SplitString(String strContent, String strSplit)
	{
		if (strContent.indexOf(strSplit) < 0)
		{
			String[] tmp = { strContent };
			return tmp;
		}
		return strContent.split("(?i)" + strSplit);
	}

	/**
	 * 清除字符右边的空格和回车
	 * 
	 * @param strSql
	 *            需要清除的字符
	 */
	public static String Rtrim(String strSql)
	{
		String s;
		int index = 0;
		for (int i = strSql.length(); i >= 0; i--)
		{
			s = strSql.substring(i, 1);
			if (s.equals(" ") || s.equals("\r") || s.equals("\n"))
			{
				index = i;
			}
		}
		return strSql.substring(index + 1);
	}

	// 清除给定字符串中的回车及换行符
	/**
	 * 清除给定字符串中的回车及换行符
	 * 
	 * @param strSql
	 *            要清除的字符串
	 */
	public static String ClearBR(String strSql)
	{

		return strSql.replace("\n", "").replace("\r", "");
	}

	// 从字符串的指定位置截取指定长度的子字符串
	/**
	 * 从字符串的指定位置截取指定长度的子字符串
	 * 
	 * @param strSql
	 *            原字符串
	 * @param startIndex
	 *            子字符串的起始位置
	 * @param length
	 *            子字符串的长度
	 */
	public static String CutString(String strSql, int startIndex, int length)
	{
		if (startIndex >= 0)
		{
			if (length < 0)
			{
				length = length * -1;
				if (startIndex - length < 0)
				{
					length = startIndex;
					startIndex = 0;
				}
				else
				{
					startIndex = startIndex - length;
				}
			}

			if (startIndex > strSql.length())
			{
				return "";
			}

		}
		else
		{
			if (length < 0)
			{
				return "";
			}
			else
			{
				if (length + startIndex > 0)
				{
					length = length + startIndex;
					startIndex = 0;
				}
				else
				{
					return "";
				}
			}
		}

		if (strSql.length() - startIndex < length)
		{
			length = strSql.length() - startIndex;
		}

		try
		{
			return strSql.substring(startIndex, length);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return strSql;
		}
	}

	// 从字符串的指定位置开始截取到字符串结尾的了符串
	/**
	 * 从字符串的指定位置开始截取到字符串结尾的了符串
	 * 
	 * @param strSql
	 *            原字符串
	 * @param startIndex
	 *            子字符串的起始位置
	 */
	public static String CutString(String strSql, int startIndex)
	{
		return CutString(strSql, startIndex, strSql.length());
	}

	// 返回 URL 字符串的编码结果
	/**
	 * 返回 URL 字符串的编码结果
	 * 
	 * @param strSql
	 *            字符串
	 */
	public static String UrlEncode(String url)
	{
		return UrlEncode(url, Encoding.UTF8);
	}

	// 返回 URL 字符串的编码结果
	/**
	 * 返回 URL 字符串的编码结果
	 * 
	 * @param str字符串
	 *            编码结果 //
	 */
	public static String UrlEncode(String url, Encoding encoding)
	{
		if (url == null)
		{
			return "";
		}
		// java.net.URLEncoder.encode(url,"GBK");
		try
		{
			return java.net.URLEncoder.encode(url.trim(), encoding.toString());
		}
		catch (UnsupportedEncodingException e)
		{
			// e.printStackTrace();
			return url;
		}

		// StringBuffer StrUrl = new StringBuffer(url.length());
		// for (int i = 0; i < url.length(); ++i)
		// {
		// switch (url.charAt(i))
		// {
		// case ' ':
		// StrUrl.append("%20");
		// break;
		// case '+':
		// StrUrl.append("%2b");
		// break;
		// case '\'':
		// StrUrl.append("%27");
		// break;
		// case '/':
		// StrUrl.append("%2F");
		// break;
		// case '.':
		// StrUrl.append("%2E");
		// break;
		// case '<':
		// StrUrl.append("%3c");
		// break;
		// case '>':
		// StrUrl.append("%3e");
		// break;
		// case '#':
		// StrUrl.append("%23");
		// break;
		// case '%':
		// StrUrl.append("%25");
		// break;
		// case '&':
		// StrUrl.append("%26");
		// break;
		// case '{':
		// StrUrl.append("%7b");
		// break;
		// case '}':
		// StrUrl.append("%7d");
		// break;
		// case '\\':
		// StrUrl.append("%5c");
		// break;
		// case '^':
		// StrUrl.append("%5e");
		// break;
		// case '~':
		// StrUrl.append("%73");
		// break;
		// case '[':
		// StrUrl.append("%5b");
		// break;
		// case ']':
		// StrUrl.append("%5d");
		// break;
		// default:
		// StrUrl.append(url.charAt(i));
		// break;
		// }
		// }
		// return StrUrl.toString();
	}

	/**
	 * 返回 URL 字符串的编码结果
	 * 
	 * @param strSql
	 *            字符串
	 */
	public static String UrlEncode(Object url)
	{
		return UrlEncode(Converts.objToStr(url), Encoding.UTF8);
	}

	/**
	 * 返回 URL 字符串的编码结果
	 * 
	 * @param strSql
	 *            字符串
	 * @param encoding
	 *            编码类型
	 */
	public static String UrlEncode(Object url, Encoding encoding)
	{
		return UrlEncode(Converts.objToStr(url), encoding);
	}

	/**
	 * 返回 URL 字符串的编码结果
	 * 
	 * @param strSql
	 *            字符串
	 * 
	 */
	public static String UrlEncode(Date url)
	{
		return UrlEncode(Converts.objToStr(url), Encoding.UTF8);
	}

	/**
	 * 返回 URL 字符串的编码结果
	 * 
	 * @param strSql
	 *            字符串
	 * @param encoding
	 *            编码类型
	 */
	public static String UrlEncode(Date url, Encoding encoding)
	{
		return UrlEncode(Converts.objToStr(url), encoding);
	}

	// 返回 URL 字符串的解码结果
	/**
	 * 返回 URL 字符串的解码结果
	 * 
	 * @param strSql
	 *            字符串
	 * 
	 */
	public static String UrlDecode(String strSql)
	{
		return UrlDecode(strSql, Encoding.UTF8);
	}

	// 返回 URL 字符串的解码结果

	/**
	 * 返回 URL 字符串的解码结果
	 * 
	 * @param strSql
	 *            字符串
	 * @param encoding
	 *            编码类型
	 */
	public static String UrlDecode(String strSql, Encoding encoding)
	{
		try
		{
			return java.net.URLDecoder.decode(strSql, encoding.toString());
		}
		catch (UnsupportedEncodingException e)
		{

			// e.printStackTrace();
			return strSql;
		}
	}

	// 格式化字节数字符串
	/**
	 * 格式化字节数字符串
	 * 
	 * @param bytes
	 *            字节数
	 */
	public static String FormatBytesStr(int bytes)
	{
		if (bytes > 1073741824)
		{
			return (double) (bytes / 1073741824) + "G";
		}
		if (bytes > 1048576)
		{
			return (double) (bytes / 1048576) + "M";
		}
		if (bytes > 1024)
		{
			return (double) (bytes / 1024) + "K";
		}
		return bytes + "Bytes";
	}

	// 为脚本替换特殊字符串
	/**
	 * 为脚本替换特殊字符串
	 * 
	 * @param strSql
	 *            原始字符
	 */
	public static String ReplaceStrToScript(String strSql)
	{
		strSql = strSql.replace("\\", "\\\\");
		strSql = strSql.replace("'", "\\'");
		strSql = strSql.replace("\"", "\\\"");
		return strSql;
	}

	// 移除Html标记
	/**
	 * 移除Html标记
	 * 
	 * @param content
	 *            原始字符
	 */
	public static String RemoveHtml(String content)
	{
		// <[^>]*>
		return content.replace("(?i)<[^>]*>", "");
	}

	// 自定义的替换字符串函数
	/**
	 * 自定义的替换字符串函数
	 * 
	 * @param SourceString
	 *            源字符串
	 * @param SearchString
	 *            要替换的字符串
	 * @param ReplaceString
	 *            替换的字符串
	 * @param IsCaseInsensetive是
	 *            否不区分大小写，true不区分，false区分 //
	 */
	public static String ReplaceString(String SourceString, String SearchString, String ReplaceString, boolean IsCaseInsensetive)
	{
		if (IsCaseInsensetive)
		{
			return SourceString.replace("(?i)" + SearchString, ReplaceString);
		}
		else
		{
			return SourceString.replace("(?i)" + SearchString, ReplaceString);
		}
	}

	// 生成指定数量的html空格符号
	/**
	 * 生成指定数量的html空格符号
	 * 
	 * public static String Spaces(int nSpaces) { StringBuilder sb = new StringBuilder(); for (int i = 0; i < nSpaces; i++) { sb.append(" &nbsp;&nbsp;"); } return sb.toString(); } // 从Email字符串中返回主机名 /** 从Email字符串中返回主机名
	 * 
	 * @param strEmail
	 *            要解析的Email字符
	 */
	public static String GetEmailHostName(String strEmail)
	{
		if (strEmail.indexOf("") < 0)
		{
			return "";
		}
		return strEmail.substring(strEmail.lastIndexOf("")).toLowerCase();
	}

	// 进行指定的替换(脏字过滤)
	/**
	 * 进行指定的替换(脏字过滤)
	 * 
	 * @param strSql
	 *            需要处理的字符串
	 * @param bantext
	 *            需要过滤的脏字，一行一个
	 */
	public static String StrFilter(String strSql, String bantext)
	{
		String text1 = "";
		String text2 = "";
		String[] textArray1 = SplitString(bantext, "\r\n");
		for (String element : textArray1)
		{
			text1 = element.substring(0, element.indexOf("="));
			text2 = element.substring(element.indexOf("=") + 1);
			strSql = strSql.replace(text1, text2);
		}
		return strSql;
	}

	// 返回相差的秒数
	/**
	 * 返回相差的秒数
	 * 
	 * @param Time
	 *            时间
	 * @param Sec
	 *            秒数
	 */
	public static int StrDateDiffSeconds(String time, int Sec)
	{
		if (time == null || time.length() == 0)
		{
			return 1;
		}
		long between = (new Date().getTime() - Converts.StrToDate(time).getTime()) / 1000;
		if (between > Integer.MAX_VALUE)
		{
			return Integer.MAX_VALUE;
		}
		else if (between < Integer.MIN_VALUE)
		{
			return Integer.MAX_VALUE;
		}
		return (int) between;
	}

	// 返回相差的分钟数
	/**
	 * 返回相差的分钟数
	 * 
	 * @param time
	 *            时间
	 * @param minutes
	 *            分钟数
	 */
	public static int StrDateDiffMinutes(String time, int minutes)
	{
		if (time == null || time.length() == 0)
		{
			return 1;
		}

		long between = (new Date().getTime() - Converts.StrToDate(time).getTime()) / 1000 / 60;
		if (between > Integer.MAX_VALUE)
		{
			return Integer.MAX_VALUE;
		}
		else if (between < Integer.MIN_VALUE)
		{
			return Integer.MAX_VALUE;
		}
		return (int) between;
	}

	// 返回相差的小时数
	/**
	 * 返回相差的小时数
	 * 
	 * @param time
	 *            时间
	 * @param hours
	 *            相差小时
	 */
	public static int StrDateDiffHours(String time, int hours)
	{
		if (time == null || time.length() == 0)
		{
			return 1;
		}

		long between = (new Date().getTime() - Converts.StrToDate(time).getTime()) / 1000 / 60 / 60;
		if (between > Integer.MAX_VALUE)
		{
			return Integer.MAX_VALUE;
		}
		else if (between < Integer.MIN_VALUE)
		{
			return Integer.MAX_VALUE;
		}
		return (int) between;
	}

	/**
	 * 去除字符前后的相同字符
	 * 
	 * @param strSql
	 *            原始字符
	 * @param strTrim
	 *            需要去除的字符
	 * @return
	 */
	public static String trim(String strSql, String strTrim)
	{
		strSql = strSql.trim();
		if (strSql.startsWith(strTrim))
		{
			strSql = strSql.substring(strTrim.length());
		}

		if (strSql.endsWith(strTrim))
		{
			strSql = strSql.substring(0, strSql.length() - strTrim.length());
		}

		return strSql;
	}

	// /**
	// * 返回字符串真实长度 可以用，暂时取消，用下面找到的网上的方式，这个方式不知道对日语韩语是否有效
	// *
	// * @param strSql
	// * 需要处理的字符串
	// */
	// public static int strLength_o(String strSql)
	// {
	// if (strSql == null)
	// return 0;
	// // strSql.getBytes("UTF-8");
	// try
	// {
	// return strSql.getBytes(__ENCODE__).length;
	// // return strSql.getBytes("UTF-8").length;
	// }
	// catch (UnsupportedEncodingException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return strSql.getBytes().length;
	// }

	/**
	 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	 * 
	 * @param char c, 需要判断的字符
	 * @return boolean, 返回true,Ascill字符
	 */
	public static boolean isLetter(char c)
	{
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 * 
	 * @param String
	 *            s ,需要得到长度的字符串
	 * @return int, 得到的字符串长度
	 */
	public static int strLength(String s)
	{
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++)
		{
			len++;
			if (!isLetter(c[i]))
			{
				len++;
			}
		}
		return len;
	}

	/**
	 * 左对齐此字符串中的字符，在右边用空格或指定的 Unicode 字符填充以达到指定的总长度。
	 * 
	 * @param strSql
	 *            原始字符串
	 * @param Len
	 *            需要设定的长度
	 * @return 新字符串
	 */
	public static String PadRight(String strSql, int Len)
	{
		return PadRight(strSql, Len, ' ', false);

	}

	/**
	 * 左对齐此字符串中的字符，在右边用空格或指定的 Unicode 字符填充以达到指定的总长度。
	 * 
	 * @param strSql
	 *            原始字符串
	 * @param Len
	 *            需要设定的长度
	 * @return 新字符串
	 */
	public static String PadRight(String strSql, int Len, Boolean isChar)
	{
		return PadRight(strSql, Len, ' ', isChar);
	}

	/**
	 * 左对齐此字符串中的字符，在右边用空格或指定的 Unicode 字符填充以达到指定的总长度。
	 * 
	 * @param strSql
	 *            原始字符串
	 * @param Len
	 *            需要设定的长度
	 * @return 新字符串
	 */
	public static String PadRight(String strSql, int Len, char chr)
	{
		return PadRight(strSql, Len, chr, false);
	}

	/**
	 * 左对齐此字符串中的字符，在右边用空格或指定的 Unicode 字符填充以达到指定的总长度。
	 * 
	 * @param strSql
	 *            原始字符串
	 * @param Len
	 *            需要设定的长度
	 * @return 新字符串
	 */
	public static String PadRight(String strSql, int Len, char chr, Boolean isChar)
	{
		if (strSql == null)
		{
			return strSql;
		}
		int strlength = strSql.length();
		if (isChar)
		{
			strlength = strLength(strSql);
		}
		if (strlength >= Len)
		{
			return strSql;
		}
		int lastLen = Len - strlength;
		StringBuilder strB = new StringBuilder(Len);
		strB.append(strSql);
		for (int i = 0; i < lastLen; i++)
		{
			strB.append(chr);
		}

		return strB.toString();
	}

	/**
	 * 右对齐此实例中的字符，在左边用空格或指定的 Unicode 字符填充以达到指定的总长度。
	 * 
	 * @param strSql
	 *            原始字符串
	 * @param Len
	 *            需要设定的长度
	 * @return 新字符串
	 */
	public static String PadLeft(String strSql, int Len)
	{
		return PadLeft(strSql, Len, ' ');
	}

	/**
	 * 右对齐此实例中的字符，在左边用空格或指定的 Unicode 字符填充以达到指定的总长度。
	 * 
	 * @param strSql
	 *            原始字符串
	 * @param Len
	 *            需要设定的长度
	 * @return 新字符串
	 */
	public static String PadLeft(String strSql, int Len, Boolean isChar)
	{
		return PadLeft(strSql, Len, ' ', isChar);
	}

	/**
	 * 右对齐此实例中的字符，在左边用空格或指定的 Unicode 字符填充以达到指定的总长度。
	 * 
	 * @param strSql
	 *            原始字符串
	 * @param Len
	 *            需要设定的长度
	 * @return 新字符串
	 */
	public static String PadLeft(String strSql, int Len, char chr)
	{
		return PadLeft(strSql, Len, chr, false);
	}

	/**
	 * 右对齐此实例中的字符，在左边用空格或指定的 Unicode 字符填充以达到指定的总长度。
	 * 
	 * @param strSql
	 *            原始字符串
	 * @param Len
	 *            需要设定的长度
	 * @return 新字符串
	 */
	public static String PadLeft(String strSql, int Len, char chr, Boolean isChar)
	{
		if (strSql == null)
		{
			return strSql;
		}
		int strlength = strSql.length();
		if (isChar)
		{
			strlength = strLength(strSql);
		}
		if (strlength >= Len)
		{
			return strSql;
		}
		int lastLen = Len - strlength;
		StringBuilder strB = new StringBuilder(Len);
		for (int i = 0; i < lastLen; i++)
		{
			strB.append(chr);
		}
		strB.append(strSql);

		return strB.toString();
	}

	/**
	 * 值为null时，返回"",否则返回其值
	 * 
	 * @param val
	 * @return
	 */
	public static String fixStringVal(String val)
	{
		if (val == null)
		{
			return "";
		}
		else
		{
			return val;
		}
	}

	// getStringArray 获取ResultSet的字段列表String[]
	/**
	 * getStringArray 获取ResultSet的字段列表String[]
	 * 
	 * @param rs
	 *            表示数据库结果集的数据表，通常通过执行查询数据库的语句生成。
	 * 
	 */
	public static String[] getStringArray(ResultSet rs)
	{
		if (rs == null)
		{
			return null;
		}
		try
		{
			ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
			int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
			String[] columnNames = new String[columnCount];
			for (int i = 0; i < columnCount; i++)
			{
				columnNames[i] = md.getColumnName(i + 1);
			}
			return columnNames;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static String __ENCODE__ = "GBK";

	// 一定要是GBK
	private static String __SERVER_ENCODE__ = "GB2312";

	/**
	 * 服务器上的缺省编码比较两字符串
	 */
	public static int compare(String s1, String s2)
	{
		String m_s1 = null, m_s2 = null;
		try
		{
			// 先将两字符串编码成GBK
			m_s1 = new String(s1.getBytes(__SERVER_ENCODE__), __ENCODE__);
			m_s2 = new String(s2.getBytes(__SERVER_ENCODE__), __ENCODE__);
		}
		catch (Exception ex)
		{
			return s1.compareTo(s2);
		}
		int res = CompareToByChinese(m_s1, m_s2);
		System.out.println("比较：" + s1 + " | " + s2 + "==== Result: " + res);
		return res;
	}

	/**
	 * 获取一个汉字字母的Char值
	 * 
	 * @param s
	 * @return
	 */
	public static int getCharCode(String s)
	{
		if (s == null || s.equals(""))
		{
			return -1;
		}
		// 保护代码
		byte[] b = s.getBytes();
		int value = 0;
		// 保证取第一个字符（汉字或者英文）
		for (int i = 0; i < b.length && i <= 2; i++)
		{
			value = value * 100 + b[i];
		}
		return value;
	}

	/**
	 * 比较两个字符串
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static int CompareToByChinese(String s1, String s2)
	{
		return CompareToByChinese(s1, s2, false);
	}

	/**
	 * 比较两个字符串
	 * 
	 * @param s1
	 * @param s2
	 * @param isUp
	 *            是否区分大小写
	 * @return
	 */
	public static int CompareToByChinese(String s1, String s2, boolean isUp)
	{
		int len1 = 0;
		int len2 = 0;
		if (isUp)
		{
			len1 = s1.length();
			len2 = s2.length();
		}
		else
		{
			len1 = s1.toLowerCase().length();
			len2 = s2.toLowerCase().length();
		}
		int n = Math.min(len1, len2);
		for (int i = 0; i < n; i++)
		{
			int s1_code = getCharCode(s1.charAt(i) + "");
			int s2_code = getCharCode(s2.charAt(i) + "");
			if (s1_code != s2_code)
			{
				return s1_code - s2_code;
			}
		}
		return len1 - len2;
	}

	// [start] 取得大写形式的字符串

	// 将数字转化为汉字的数组,因为各个实例都要使用所以设为静态
	private static final char[] cnNumbers = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' };

	// 供分级转化的数组,因为各个实例都要使用所以设为静态
	private static final char[] series = { '元', '拾', '百', '仟', '万', '拾', '百', '仟', '亿' };

	/**
	 * 取得大写形式的字符串
	 * 
	 * @return
	 */
	public static String getCnString(double original)
	{
		return getCnString(original + "");
	}

	/**
	 * 取得大写形式的字符串
	 * 
	 * @return
	 */
	public static String getCnString(String original)
	{
		// 整数部分
		String integerPart;
		// 小数部分
		String floatPart;

		// 成员变量初始化
		integerPart = "";
		floatPart = "";

		if (original.contains("."))
		{
			// 如果包含小数点
			int dotIndex = original.indexOf(".");
			integerPart = original.substring(0, dotIndex);
			floatPart = original.substring(dotIndex + 1);
		}
		else
		{
			// 不包含小数点
			integerPart = original;
		}

		// 因为是累加所以用StringBuffer
		StringBuffer sb = new StringBuffer();

		// 整数部分处理
		for (int i = 0; i < integerPart.length(); i++)
		{
			int number = getNumber(integerPart.charAt(i));

			sb.append(cnNumbers[number]);
			sb.append(series[integerPart.length() - 1 - i]);
		}

		// 小数部分处理
		if (floatPart.length() > 0)
		{
			sb.append("点");
			for (int i = 0; i < floatPart.length(); i++)
			{
				int number = getNumber(floatPart.charAt(i));

				sb.append(cnNumbers[number]);
			}
		}

		// 返回拼接好的字符串
		return sb.toString();
	}

	/**
	 * 将字符形式的数字转化为整形数字 因为所有实例都要用到所以用静态修饰
	 * 
	 * @param c
	 * @return
	 */
	private static int getNumber(char c)
	{
		String strSql = String.valueOf(c);
		return Integer.parseInt(strSql);
	}

	// [end]

	/**
	 * 过滤Sql代码里面的不合法字符
	 * @param strSql
	 * @return
	 */
	public String filterSql(String strSql)
	{
		if (strSql == null)
			return "";
		return strSql.replaceAll(".*([';]+|(--)+).*", " ");
	}
	
	/**
	 * 返回邮件链接地址
	 * @param email
	 * @return
	 */
	public static String getMailAddress(String email) {
		if (email == null)
			return "";
		int wz = email.indexOf("@");
		if (wz == -1)
			return "";
		String emailAddr = "http://mail." + email.substring(wz + 1);
		if (email.indexOf("hotmail.com") > 0) {
			emailAddr = "http://" + email.substring(wz + 1);
		}
		return emailAddr;
	}
}