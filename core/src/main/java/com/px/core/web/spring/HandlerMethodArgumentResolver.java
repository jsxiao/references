package com.px.core.web.spring;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.px.core.security.UserProfile;
import com.px.core.security.shiro.user.UserContext;


/**
 * 
 * @author jason
 *
 */
public class HandlerMethodArgumentResolver implements org.springframework.web.method.support.HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> clz = parameter.getParameterType();

		if(UserProfile.class.equals(clz)){
			return true;
		}

		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		Class<?> clz = parameter.getParameterType();

		//HttpServletRequest req = ContextUtils.getRequest();
		
		if (UserProfile.class.equals(clz))
			return UserContext.getUser();

		return null;
	}
}
