package com.aggrepoint.dao.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.cache.interceptor.DefaultKeyGenerator;

import antlr.collections.List;

public class KeyGenerator extends DefaultKeyGenerator {
	@Override
	public Object generate(Object target, Method method, Object... params) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		Class entityClass =target.getClass();
		buffer.append(entityClass.getName());
		buffer.append(method.getName());
		if (params != null && params.length >= 1) {
			for (Object obj : params) {
				if (obj != null) {
					if (obj instanceof AtomicInteger
							|| obj instanceof AtomicLong
							|| obj instanceof BigDecimal
							|| obj instanceof BigInteger || obj instanceof Byte
							|| obj instanceof Double || obj instanceof Float
							|| obj instanceof Integer || obj instanceof Long
							|| obj instanceof Short) {
						buffer.append(obj);
					} else if (obj instanceof List || obj instanceof Set
							|| obj instanceof Map) {
						buffer.append(obj);
					} else {
						buffer.append(obj.hashCode());
					}
				}
			}
		}
		int keyGenerator = buffer.toString().hashCode();
		return Integer.valueOf(keyGenerator);

	}
}
