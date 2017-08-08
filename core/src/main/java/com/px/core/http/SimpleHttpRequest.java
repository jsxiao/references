package com.px.core.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.px.core.utils.Encoding;

public class SimpleHttpRequest implements HttpRequest{

	protected static final Logger logger = LoggerFactory.getLogger(SimpleHttpRequest.class);
	private static final CloseableHttpClient httpClient;
	
	static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }
	
	public static HttpClient getClient(){
		return httpClient;
	}
	
	/**
	 * @see com.px.core.http.SSLHttpRequest#execute(String, Map, SSLProvider)
	 */
	@Override
	public ResponseBody execute(String url, Map<String, String> params, SSLProvider provider) {
		return null;
	}
	
	@Override
	public ResponseBody execute(String url, String params, Encoding encoding) {
		return doXmlPost(url, params, encoding);
	}
	
	@Override
	public ResponseBody execute(HttpMethod method, String url, Map<String, String> params, Encoding encoding) {
		if(method == HttpMethod.GET)
			return doGet(url, params, encoding);
		
		if(method == HttpMethod.POST)
			return doPost(url, params, encoding);
		
		throw new HttpRequestMethodNotSupportedException(method.toString());
	}
	
	/**
     * HTTP Get 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params	请求的参数
     * @param charset	编码格式
     * @return	页面内容
     */
    public static ResponseBody doGet(String url, Map<String, String> params, Encoding encoding){
    	if(StringUtils.isBlank(url)){
    		return null;
    	}
    	
    	CloseableHttpResponse response = null;
    	try {
    		if(params != null && !params.isEmpty()){
    			List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
    			for(Map.Entry<String,String> entry : params.entrySet()){
    				String value = entry.getValue();
    				if(value != null){
    					pairs.add(new BasicNameValuePair(entry.getKey(),value));
    				}
    			}
    			url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, encoding.toString()));
    		}
    		HttpGet httpGet = new HttpGet(url);
    		response = httpClient.execute(httpGet);
    		int statusCode = response.getStatusLine().getStatusCode();
    		if (statusCode != 200) {
    			httpGet.abort();
    			throw new RuntimeException("HttpClient,error status code :" + statusCode);
    		}
    		HttpEntity entity = response.getEntity();
    		String result = null;
    		if (entity != null){
    			result = EntityUtils.toString(entity, "utf-8");
    		}
    		/*EntityUtils.consume(entity);
    		response.close();*/
    		return new DefaultResponseBody(statusCode, result);
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    	}finally{
        	HttpClientUtils.closeQuietly(response);
    	}
    	return null;
    }
    
    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params	请求的参数
     * @param charset	编码格式
     * @return	页面内容
     */
    public static ResponseBody doPost(String url, Map<String,String> params, Encoding encoding){
    	if(StringUtils.isBlank(url)){
    		return null;
    	}
    	
    	CloseableHttpResponse response = null;
    	try {
    		List<NameValuePair> pairs = null;
    		if(params != null && !params.isEmpty()){
    			pairs = new ArrayList<NameValuePair>(params.size());
    			for(Map.Entry<String,String> entry : params.entrySet()){
    				String value = entry.getValue();
    				if(value != null){
    					pairs.add(new BasicNameValuePair(entry.getKey(),value));
    				}
    			}
    		}
    		HttpPost httpPost = new HttpPost(url);
    		if(pairs != null && pairs.size() > 0){
    			httpPost.setEntity(new UrlEncodedFormEntity(pairs, encoding.toString()));
    		}
    		response = httpClient.execute(httpPost);
    		int statusCode = response.getStatusLine().getStatusCode();
    		if (statusCode != 200) {
    			httpPost.abort();
    			throw new RuntimeException("HttpClient,error status code :" + statusCode);
    		}
    		HttpEntity entity = response.getEntity();
    		String result = null;
    		if (entity != null){
    			result = EntityUtils.toString(entity, "utf-8");
    		}
    		/*EntityUtils.consume(entity);
    		response.close();*/
    		return new DefaultResponseBody(statusCode, result);
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    	}finally{
    		HttpClientUtils.closeQuietly(response);
    	}
    	return null;
    }
    
    public static ResponseBody doXmlPost(String url, String params, Encoding encoding){
    	if(StringUtils.isBlank(url) || StringUtils.isBlank(params)){
    		return new DefaultResponseBody(-1, String.format("无效的请求地址或参数: {}; {}", url, params));
    	}
    	
    	CloseableHttpResponse response = null;
    	try {
    		HttpPost httpPost = new HttpPost(url);
    		
    		httpPost.addHeader("Content-Type", "text/xml;charset=UTF-8");  
    		httpPost.addHeader("Accept", "*/*");
    		
    		// 内容进行编码
    		StringEntity myEntity = new StringEntity(params, encoding.toString());  
    		httpPost.setEntity(myEntity);  
			
    		response = httpClient.execute(httpPost);
    		int statusCode = response.getStatusLine().getStatusCode();
    		if (statusCode != 200) {
    			httpPost.abort();
    			throw new RuntimeException("HttpClient,error status code :" + statusCode);
    		}
    		HttpEntity entity = response.getEntity();
    		String result = null;
    		if (entity != null){
    			result = EntityUtils.toString(entity, "utf-8");
    		}
    		/*EntityUtils.consume(entity);
    		response.close();*/
    		return new DefaultResponseBody(statusCode, result);
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    	}finally{
    		HttpClientUtils.closeQuietly(response);
    	}
    	return null;
    }
    
    
    public static void main(String[] args) {
    	HttpRequest request = new SimpleHttpRequest();
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("xmlStr", "<Group Login_Name=\"hljxc\" Login_Pwd=\"121B7583CC71DAFC6A30519F5735962D\" OpKind=\"0\" InterFaceID=\"\" SerType=\"repay\"><E_Time></E_Time><Item><Task><Recive_Phone_Number>18944502637</Recive_Phone_Number><Content><![CDATA[【友情提示】$username$先生/ 女士，公司受出借人委托请您在$repaymentTime$前将$account$元存入您江西银行尾号$cardNo$借记卡上，以免影响您的信用记录，祝您工作顺利。]]></Content><Search_ID>20161023000374015928</Search_ID></Task></Item></Group>");
    	ResponseBody execute = request.execute("http://userinterface.vcomcn.com/Opration.aspx", params.get("xmlStr"), Encoding.GB2312);
    	System.out.println(execute.getStatus()+"--"+execute.getBody());
	}
}
