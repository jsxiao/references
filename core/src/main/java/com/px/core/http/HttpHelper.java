package com.px.core.http;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.px.core.utils.Encoding;

/**
 * http助手
 * <pre>
 	林花谢了春红，
	太匆匆，
	无奈朝来寒雨晚来风。
		 
 	胭脂泪，
	相留醉，
	几时重，
	自是人生长恨水长东。
 * </pre>
 * @author Jason
 *
 */
public final class HttpHelper {

	private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);
	private static HttpRequest request = new SimpleHttpRequest();
	private static HttpRequest sslRequest = new SSLHttpRequest();
	private static int errorCode = -1;
	
	/**
	 * 返回一个Client
	 * @return
	 */
	public static HttpClient getHttpClient(){
		return SimpleHttpRequest.getClient();
	}
	
	/**
	 * POST请求
	 * @param url 请求地址
	 * @param params 参数
	 * @return
	 */
	public static ResponseBody doPost(String url, Map<String, String> params){
		if(StringUtils.isBlank(url)){
			logger.error("无法进行POST请求, 请求地址参数不正确", url);
			return new DefaultResponseBody(errorCode, null);
		}
		
		ResponseBody execute = request.execute(HttpMethod.POST, url, params, Encoding.UTF8);
		return execute == null ? new DefaultResponseBody(errorCode, null) : execute;
	}
	
	/**
	 * GET请求
	 * @param url 请求地址
	 * @param params 参数
	 * @return
	 */
	public static ResponseBody doGet(String url, Map<String, String> params){
		
		if(StringUtils.isBlank(url)){
			logger.error("无法进行GET请求, 请求地址参数不正确", url);
			return new DefaultResponseBody(errorCode, null);
		}
		
		ResponseBody execute = request.execute(HttpMethod.GET, url, params, Encoding.UTF8);
		return execute == null ? new DefaultResponseBody(errorCode, null) : execute;
	}

	/**
	 * SSL请求
	 * @param url
	 * @param params
	 * @param provider
	 * @return
	 */
	public static ResponseBody doSSLRequest(String url, Map<String, String> params, SSLProvider provider){
		ResponseBody execute = sslRequest.execute(url, params, provider);
		return execute == null ? new DefaultResponseBody(errorCode, null) : execute;
	}
	
	/**
	 * XML方式POST请求
	 * @see Encoding
	 * @param url 请求地址
	 * @param xmlStr xml参数
	 * @param encoding 编码方式
	 * @return
	 */
	public static ResponseBody doXMLRequest(String url, String xmlStr, Encoding encoding){
		ResponseBody execute = request.execute(url, xmlStr, encoding);
		return execute == null ? new DefaultResponseBody(errorCode, null) : execute;
	}
	
	public static ResponseBody doXMLRequest(String url, String xmlStr){
		return doXMLRequest(url, xmlStr, Encoding.UTF8);
	}
}
