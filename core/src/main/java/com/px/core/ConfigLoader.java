package com.px.core;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.px.core.cache.redis.IRedisHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Maps;
import com.px.utils.NameValuePair;

public class ConfigLoader {

	private static Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
	@Deprecated
	private static Map<String, NameValuePair> cacheMap;
	@Deprecated
	private ConfigLoaderService loader;

	public static ConfigLoader INSTANCE = new ConfigLoader();
	private static IRedisHelper redisHelper;

	private ConfigLoader(){}

	/*public ConfigLoader(){
		cacheMap = Maps.newConcurrentMap();
	}*/
	
	public NameValuePair get(String key){

		if(redisHelper == null){
			redisHelper = ContextHolder.get().getBean(IRedisHelper.class);
		}

		NameValuePair pair = null;
		// 先从redis取, 如不存在再读数据库
		if(ContextHolder.get() != null){
			String value = redisHelper.get("sysinfo:"+key);
			try{
				Gson gson = new Gson();
				Config config = gson.fromJson(value, Config.class);
				pair = config.getValue();
			}catch(Exception e){}
		}
		return pair;
	}

	@Deprecated
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
