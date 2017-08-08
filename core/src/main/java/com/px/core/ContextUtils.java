package com.px.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.ContextLoader;

import com.px.core.security.UserProfile;
import com.px.core.security.shiro.user.UserContext;
import com.px.core.utils.WebUtil;

public class ContextUtils {

	private static ThreadLocal<HttpServletRequest> req = new ThreadLocal<>();
	private static ThreadLocal<HttpServletResponse> res = new ThreadLocal<>();
	
	public static void setResponse(HttpServletResponse response){
		res.set(response);
	}
	
	public static void setRequest(HttpServletRequest request){
		req.set(request);
	}
	
	public static HttpServletResponse getResponse(){
		return res.get();
	}
	
	public static HttpServletRequest getRequest(){
		return req.get();
	}
	
	public static String getRequestURI(){
		return getRequest().getRequestURI();
	}
	
	public static String getParameter(String name){
		return getRequest().getParameter(name);
	}
	
	public static String getParameter(String name, String defVal){
		String value = getParameter(name);
		return value == null ? defVal : value;
	}
	
	public static HttpSession getSession(){
		return getRequest().getSession(true);
	}
	
	public static HttpSession getSession(boolean create){
		return getRequest().getSession(create);
	}
	
	public static <T> T getSessionAttribute(String name){
		return getSessionAttribute(name, true);
	}
	
	public static <T> T getSessionAttribute(String name, boolean create){
		HttpSession session = getRequest().getSession(create);
		if(session != null)
			return (T) session.getAttribute(name);
		return null;
	}
	
	public static void removeSessionAttribute(String name){
		HttpSession session = getRequest().getSession(false);
		if(session != null) session.removeAttribute(name);
	} 
	
	public static void setSessionAttribute(String name, Object value){
		getSession().setAttribute(name, value);
	}
	
	public static String getRequestIp(){
		return WebUtil.getIpAddress(getRequest());
	}
	
	public static String getRealPath(){
		return ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
	}
	
	/**
	 * 判断当前用户是否登录
	 * @return
	 */
	public static boolean isAnonymousUser(){
		return UserContext.getUser().isAnonymous();
	}
	
	/**
	 * 返回当前登录用户
	 * @return
	 */
	public static UserProfile getUser(){
		return UserContext.getUser();
	}
}
