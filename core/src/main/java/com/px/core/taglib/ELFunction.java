package com.px.core.taglib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.px.core.security.UserProfile;
import com.px.core.security.shiro.user.UserContext;
import com.px.utils.StringHelper;

public class ELFunction {

	private final static Logger logger = LoggerFactory.getLogger(ELFunction.class);
	
	public static UserProfile u(){
		UserProfile user = UserContext.getUser();
		if(logger.isDebugEnabled())
			logger.debug("get UserProfile from UserContext. is anonymouse ? {}", user.isAnonymous());
		return user;
	}
	
	public static String hideChar(String str){
		if(!StringUtils.isEmpty(str))
			return StringHelper.hideChar2(str);
		
		return ""; 
	}
}
