package com.px.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.px.core.ConfigLoader;
import com.px.core.PropertiesLoader;

/**
 * 配置文件工具类
 * @author Jason
 *
 */
public class ConfigUtil {

	private static Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
	private static PropertiesLoader propertiesLoader = null;

	/**
	 * 该方法应当仅在初始化时调用
	 * @param properties
	 */
	public static void loadProperties(String... properties){
		propertiesLoader = new PropertiesLoader(properties);
	}

	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		if(logger.isDebugEnabled())
			logger.debug("get property from given key: {}", key);
		return propertiesLoader.getProperty(key);
	}
	
	/**
	 * 返回系统配置(数据库中的数据)
	 * @param key
	 * @return
	 */
	public static NameValuePair getSysConfig(String key){
		if(StringUtils.isEmpty(key))
			return null;
		
		return ConfigLoader.get(key);
	}
}
