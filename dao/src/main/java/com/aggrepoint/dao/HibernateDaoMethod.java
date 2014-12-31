package com.aggrepoint.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 * 
 */
public interface HibernateDaoMethod<T> {
	public Object invoke(Object proxy, Method method, Object[] args)
			throws NoSuchMethodException, InvocationTargetException,
			IllegalAccessException;
}
