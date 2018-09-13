package com.px.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.px.core.Config;
import com.px.core.ContextHolder;
import com.px.core.cache.redis.IRedisHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.px.utils.ConfigUtil;

public final class Global {

	private static Logger logger = LoggerFactory.getLogger(Global.class);
	
	public static String getValue(String key) {

		NameValuePair pair = ConfigUtil.getSysConfig(key);
		if(pair == null)
			return "";
		
		String value = pair.getValue();
		
		if(logger.isDebugEnabled())
			logger.debug("get value from SystemInfo. key:{}, value:{}", key, value);
		
		if (!StringUtils.hasText(value)) 
			return "";
		else
			return value;
	}
	
	public static String getWebPath(){
		return Global.class.getClassLoader().getResource("").toString().replaceFirst("file:", "");
	}
	
	public static String getString(String key) {
		return getValue(key);
	}

	public static int getInt(String key) {
		return Integer.parseInt(getValue(key));
	}

	public static double getDouble(String key) {
		return Double.parseDouble(getValue(key));
	}
}
