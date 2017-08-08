package com.px.core.http;

import java.util.Map;

import org.springframework.http.HttpMethod;

import com.px.core.utils.Encoding;

public interface HttpRequest {
	
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String CHARSET_GB2312 = "UTF-8";
	
	/**
	 * http请求
	 * @param method
	 * @param url
	 * @param params
	 * @param encoding
	 * @return
	 */
	public ResponseBody execute(HttpMethod method, String url, Map<String, String> params, Encoding encoding);
	
	/**
	 * http请求; xml参数; 请求方式为post
	 * @param method
	 * @param url
	 * @param params
	 * @param encoding
	 * @return
	 */
	public ResponseBody execute(String url, String params, Encoding encoding);
	
	
	/**
	 * SSL请求
	 * @param url
	 * @param params
	 * @param provider 证书提供以及签名
	 * @return
	 */
	public ResponseBody execute(String url, Map<String, String> params, SSLProvider provider);
}
