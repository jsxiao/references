package com.ec.validator;

import java.util.Map;

import org.springframework.validation.Errors;

import com.google.common.collect.Maps;

/**
 * 基础验证
 * @author jason
 *
 */
public class BasValidator {

	Map<String, String> errors = Maps.newHashMap();
	
	public void addError(String key, String value, Errors error){
		error.reject("error");
		errors.put(key, value);
	}
	
	public boolean hasErrors(){
		return !errors.isEmpty();
	}
	
	public Map<String, String> getErrors(){
		return this.errors;
	}
}
