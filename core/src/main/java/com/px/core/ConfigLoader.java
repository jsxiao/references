package com.px.core;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Maps;
import com.px.utils.NameValuePair;

public class ConfigLoader {

	private static Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
	private static Map<String, NameValuePair> cacheMap;
	private ConfigLoaderService loader;
	
	public ConfigLoader(){
		cacheMap = Maps.newConcurrentMap();
	}
	
	public static NameValuePair get(String key){
		return cacheMap.get(key);
	}
	
	public void loadConfig(){
		List<Config> configs = loader.getAll();
		if(CollectionUtils.isEmpty(configs))
			return;
		
		for(Config c : configs){
			if(StringUtils.isEmpty(c.getKey()))
				continue;
			
			if(logger.isDebugEnabled())
				logger.debug("load config: {}", c);
			
			cacheMap.put(c.getKey(), c.getValue());
		}
	}

	public ConfigLoaderService getLoader() {
		return loader;
	}

	public void setLoader(ConfigLoaderService loader) {
		this.loader = loader;
	}
}
