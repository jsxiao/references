package com.ec.plugin;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ec.cinlet.ConfigProvider;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

public class DefaultConfigProvider implements ConfigProvider, Serializable{
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DefaultConfigProvider.class);
	
	@Override
	public String getConfig(String name) {
		String result = "";
		try {
			result = CacheBuilder.newBuilder().maximumSize(1000).weakKeys().softValues().refreshAfterWrite(120, TimeUnit.SECONDS).expireAfterWrite(10, TimeUnit.MINUTES)
					.build(new CacheLoader<String, String>() {
						@Override
						public String load(String key) throws Exception {
							String result = null;
							try {
								String[] keys = key.split("\\.");
								if (keys.length == 2)
									result = ""; //configService.findByModuleCodeAndCfgName(keys[0], keys[1]);
							}
							catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
							return result;
						}
					}).get(name);
		}
		catch (ExecutionException e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
}
