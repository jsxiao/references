package com.px.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Jason
 *
 */
public final class ContextHolder implements ApplicationContextAware{

	private static ApplicationContext ctx;
	
	public static ApplicationContext get(){
		return ctx;
	}
	
	public static <T> T get(Class<T> clazz){
		return get().getBean(clazz);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ctx = applicationContext;
	}
}
