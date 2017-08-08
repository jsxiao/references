package com.px.core.http;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.px.core.utils.Encoding;

public class SSLHttpRequest implements HttpRequest {

	private static Logger logger = LoggerFactory.getLogger(SSLHttpRequest.class);
	
	@Override
	public ResponseBody execute(String url, Map<String, String> params, SSLProvider provider) {

		if(!StringUtils.hasLength(url)){
			logger.warn("无法进行SSL请求, 地址无效: {}", url);
			return null;
		}
		
		if(CollectionUtils.isEmpty(params)){
			logger.warn("无法进行SSL请求, 参数无效: {}", params);
			return null;
		}
		
		if(provider == null){
			logger.warn("无法进行SSL请求, provider无效: {}", provider);
			return null;
		}
		
		logger.info("SSL请求: {}, 参数: {}", url, params);
		
		try{
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Accept-Charset", "UTF-8");
			
			// 处理签名
			provider.sign(params);

			// 证书校验
			trustAllHttpsCertificates();
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
			
			// 发送请求
			//HttpEntity<String> entity = new HttpEntity(new Gson().toJson(params), headers);
			HttpEntity<String> entity = new HttpEntity(JSON.toJSON(params), headers);
			ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
			
			if(response == null){
				logger.warn("SSL请求没有返回任何内容! [{}]", response);
				return new DefaultResponseBody(-1, null);
			}
			
			// 响应报文
			logger.info("SSL请求返回: {}", response.getBody().toString());
			return new DefaultResponseBody(response.getStatusCode().value(), response.getBody());
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * @see com.px.core.http.SimpleHttpRequest#execute(String, String, Encoding)
	 */
	@Override
	public ResponseBody execute(HttpMethod method, String url, Map<String, String> params, Encoding encoding) {
		return null;
	}

	/**
	 * @see com.px.core.http.SimpleHttpRequest#
	 */
	@Override
	public ResponseBody execute(String url, String params, Encoding encoding) {
		return null;
	}

	/**
	 * 忽略证书
	 */
	static HostnameVerifier hv = new HostnameVerifier() {
		public boolean verify(String urlHostName, SSLSession session) {
			logger.info("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
			return true;
		}
	};

	/**
     * 证书校验
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static void trustAllHttpsCertificates() throws NoSuchAlgorithmException, KeyManagementException {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }

	static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

	}
}
