package com.px.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class PropertiesLoader {

	private static Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);
	public static PropertiesLoader INSTANCE = null;
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	private final Properties properties;
	private boolean isReloading = false;


	public PropertiesLoader(String... resourcesPaths) {
		INSTANCE = this;
		properties = loadProperties(resourcesPaths);
	}

	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * 取出Property，但以System的Property优先,取不到返回空字符串.
	 * @param key
	 * @return
	 */
	private String getValue(String key) {
		String systemProperty = System.getProperty(key);
		if (systemProperty != null) {
			return systemProperty;
		}
		if (properties.containsKey(key)) {
	        return properties.getProperty(key);
	    }
	    return "";
	}

	/**
	 * 取出String类型的Property，但以System的Property优先,如果都为Null则抛出异常.
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {

		if(isReloading){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return value;
	}

	/**
	 * 取出String类型的Property，但以System的Property优先.如果都为Null则返回Default值.
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(String key, String defaultValue) {
		String value = getValue(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * 取出Integer类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
	 * @param key
	 * @return
	 */
	public Integer getInteger(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Integer.valueOf(value);
	}

	/**
	 * 取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Integer getInteger(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}

	/**
	 * 取出Double类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
	 * @param key
	 * @return
	 */
	public Double getDouble(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Double.valueOf(value);
	}

	/**
	 * 取出Double类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Double getDouble(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Double.valueOf(value) : defaultValue;
	}

	/**
	 * 取出Boolean类型的Property，但以System的Property优先.如果都为Null抛出异常,如果内容不是true/false则返回false.
	 * @param key
	 * @return
	 */
	public Boolean getBoolean(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Boolean.valueOf(value);
	}

	/**
	 * 取出Boolean类型的Property，但以System的Property优先.如果都为Null则返回Default值,如果内容不为true/false则返回false.
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Boolean getBoolean(String key, boolean defaultValue) {
		String value = getValue(key);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}

	public void reLoad(String resources){
		Properties props = new Properties();
		logger.debug("Loading properties file from: {}", resources);

		InputStream is = null;
		try {
			is = new FileInputStream(resources);
			props.load(is);

			Enumeration<?> enumeration = props.propertyNames();
			while(enumeration.hasMoreElements()){
				String key = (String) enumeration.nextElement();
				String value = props.getProperty(key);
				properties.setProperty(key, value);
			}
		} catch (IOException ex) {
			logger.warn("Could not load properties from path: {};  {}", resources, ex.getMessage());
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	private Properties loadProperties(String... resourcesPaths) {
		Properties props = new Properties();

		for (String location : resourcesPaths) {

			logger.debug("Loading properties file from: {}", location);

			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				props.load(is);
			} catch (IOException ex) {
				logger.warn("Could not load properties from path: {};  {}", location, ex.getMessage());
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
		return props;
	}

	public static void main(String[] args) {
		ClassPathResource resource = new ClassPathResource("resources");
		System.out.println(resource.getPath());
	}
}
