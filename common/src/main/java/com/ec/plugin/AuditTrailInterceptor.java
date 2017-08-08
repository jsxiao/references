package com.ec.plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.StringUtils;

import tk.mybatis.mapper.annotation.CreateBy;
import tk.mybatis.mapper.annotation.IdGenerator;
import tk.mybatis.mapper.annotation.UpdateBy;

/**
 * 
 * @author jason
 *
 */
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class AuditTrailInterceptor implements Interceptor, InitializingBean{

	private com.ec.plugin.IdGenerator idGen = null;
	
	private String getId() {
		return getIdGen().getUUID();
	}
	
	public com.ec.plugin.IdGenerator getIdGen(){
		return this.idGen;
	}
	
	public void setIdGen(com.ec.plugin.IdGenerator idGen){
		this.idGen = idGen;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.idGen == null) {
			this.idGen = new DefaultIdGenerator();
		}
	}
	
	private <T> void invokeMethod(Class<?> clazz, Object target, String fieldName, Class<?> type, T value){
		String methodName = "set".concat(StringUtils.capitalize(fieldName));
		Method setMethod = ReflectionUtils.findMethod(clazz, methodName, type);
		
		Assert.notNull(setMethod, "无法为[@IdGenerator, @CreateBy, @UpdateBy]标注的属性赋值, 请确认有对应的setter方法.");
		
		ReflectionUtils.invokeMethod(setMethod, target, value);
	}
	
	private void chooseInvoke(Class<?> clazz, Object target, Field field){
		Class<?> type = field.getType();
		Object value = type.equals(java.lang.String.class) ? new java.lang.String("admin") : new java.util.Date();
		invokeMethod(clazz, target, field.getName(), type, value);
	}
	
	private void updaterInvoke(Class<?> clazz, Object target, Field field){
		if(field.getAnnotation(UpdateBy.class) != null){
			chooseInvoke(clazz, target, field);
		}
	}
	
	private void createrInvoke(Class<?> clazz, Object target, Field field){
		if(field.getAnnotation(CreateBy.class) != null){
			chooseInvoke(clazz, target, field);
		}
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
		
		Class<?> clazz = entity.getClass();
		
		final List<Field> fields = new ArrayList<Field>(3);
		ReflectionUtils.doWithFields(clazz, new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				
				if (field.getAnnotation(IdGenerator.class) != null
						|| field.getAnnotation(CreateBy.class) != null
						|| field.getAnnotation(UpdateBy.class) != null) {
					fields.add(field);
				}
			}
		});
		
		if(!fields.isEmpty()){
			for(Field field : fields){
				// 设置ID
				if(field.getAnnotation(IdGenerator.class) != null){
					invokeMethod(clazz, entity, field.getName(), java.lang.String.class, getId());
				}
				
				if(!isInsert){
					updaterInvoke(clazz, entity, field);
				}else{
					createrInvoke(clazz, entity, field);
					updaterInvoke(clazz, entity, field);
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
