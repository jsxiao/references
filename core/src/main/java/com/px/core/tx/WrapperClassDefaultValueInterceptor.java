package com.px.core.tx;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.px.core.tx.annotation.RequiredDef;

@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class WrapperClassDefaultValueInterceptor implements Interceptor, InitializingBean{

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	private <T> void invokeSetMethod(Class<?> clazz, Object target, String fieldName, Class<?> type, T value){
		String methodName = "set".concat(StringUtils.capitalize(fieldName));
		Method setMethod = ReflectionUtils.findMethod(clazz, methodName, type);
		
		Assert.notNull(setMethod);
		
		ReflectionUtils.invokeMethod(setMethod, target, value);
	}
	
	private Object invokeGetMethod(Class<?> clazz, Object target, String fieldName, Class<?> type){
		String methodName = "get".concat(StringUtils.capitalize(fieldName));
		Method setMethod = ReflectionUtils.findMethod(clazz, methodName);
		
		Assert.notNull(setMethod);
		
		return ReflectionUtils.invokeMethod(setMethod, target);
	}
	
	public void setZeroVal(Class<?> clazz, Object target, String fieldName, Class<?> type){
		
		if(invokeGetMethod(clazz, target, fieldName, type) != null)
			return;
		
		if(type.equals(Integer.class))
			invokeSetMethod(clazz, target, fieldName, type, new Integer(0));
		else if(type.equals(Long.class))
			invokeSetMethod(clazz, target, fieldName, type, new Long(0));
		else if(type.equals(Double.class))
			invokeSetMethod(clazz, target, fieldName, type, new Double(0));
		else if(type.equals(BigDecimal.class))
			invokeSetMethod(clazz, target, fieldName, type, new BigDecimal(0));
	}
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
		Object entity = invocation.getArgs()[1];
		
		// 判断新增还是修改
		boolean isInsert = false;
		String msId = ms.getId();
		if(msId.contains(".create") || msId.contains(".save") || msId.contains(".insert") || msId.contains(".add")){
			isInsert = true;
		}
		
		if(isInsert && entity != null){
			Class<?> clazz = entity.getClass();
			final List<Field> fields = Lists.newArrayList();
			ReflectionUtils.doWithFields(clazz, new FieldCallback() {
				@Override
				public void doWith(Field field) throws IllegalArgumentException,
						IllegalAccessException {
					
					if (field.getAnnotation(RequiredDef.class) != null) {
						fields.add(field);
					}
				}
			});

			if(!CollectionUtils.isEmpty(fields)){
				for(Field field : fields){
					setZeroVal(clazz, entity, field.getName(), field.getType());
				}
			}
		}
		
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
		
	}

}
