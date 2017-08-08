package com.px.core.http;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class DefaultResponseBody implements ResponseBody{

	protected static final Logger logger = LoggerFactory.getLogger(DefaultResponseBody.class);
	
	/**
	 * 响应内容
	 */
	private String body;
	
	/**
	 * 返回状态
	 */
	private int status;
	
	private Object obj;
	
	
	public DefaultResponseBody(int status, String body){
		this.status = status;
		this.body = body;
	}
	
	public DefaultResponseBody(int status, Object body){
		this.status = status;
		this.obj = body;
	} 
	
	@Override
	public String getBody() {
		return this.body;
	}

	@Override
	public int getStatus() {
		return this.status;
	}

	@Override
	public <T> T toObject(Class<T> clazz) {
		Gson gson = new Gson();
		if(StringUtils.isBlank(getBody()))
			return null;
		
		try{
			return gson.fromJson(getBody(), clazz);
		}catch(JsonSyntaxException e){
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getObject() {
		return (T) this.obj;
	}
}
