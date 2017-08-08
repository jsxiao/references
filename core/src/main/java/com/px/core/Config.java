package com.px.core;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.px.utils.NameValuePair;

public class Config implements Serializable {
	private static final long serialVersionUID = 3706356875225450626L;

	private String key;
	private NameValuePair value;
	
	public Config() {}
	
	public Config(String key, NameValuePair value){
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public NameValuePair getValue() {
		return value;
	}

	public void setValue(NameValuePair value) {
		this.value = value;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String toString(){
    	return ReflectionToStringBuilder.toString(this);
    }
}
