package com.px.core.web.filter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.px.core.utils.Converts;
import com.px.core.utils.StringFormat;

/**
 * 防止SQL注入
 * 
 * @author jason
 * @date 2016年4月1日下午3:21:27
 */
public class UrlFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		//HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		// 有sql关键字，跳转到error.html
		String str = StringFormat.UrlDecode(Converts.objToStr(request.getQueryString()));
		if (// sqlValidate(str)// 验证sql关键字
		isContains("</?[a-z][a-z0-9]*[^<>]*>", str)// 验证html标签
				|| isContains("(?i)<script.*>", str)// 验证script标签
		) {
			throw new IOException("您发送请求中的参数中含有非法字符");
		} else {
			chain.doFilter(req, res);
		}

	}

	// 效验
	protected static boolean sqlValidate(String str) {
		str = str.toLowerCase();// 统一转为小写
		String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|"
				+ "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|"
				+ "table|from|grant|use|group_concat|column_name|"
				+ "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|"
				+ "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";// 过滤掉的sql关键字，可以手动添加
		String[] badStrs = badStr.split("\\|");
		for (int i = 0; i < badStrs.length; i++) {
			if (str.indexOf(badStrs[i]) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 测试表达式
	 * 
	 * @param regexstr
	 * @param str
	 * @return
	 */
	private static boolean isContains(String regexstr, String str) {

		Pattern regex = Pattern.compile(regexstr, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher regexMatcher = regex.matcher(str);
		return regexMatcher.find();

	}
}
