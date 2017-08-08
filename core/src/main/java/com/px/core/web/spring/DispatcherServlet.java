package com.px.core.web.spring;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import com.px.core.ContextUtils;

public class DispatcherServlet extends org.springframework.web.servlet.DispatcherServlet{
	private static final long serialVersionUID = 2831962106561303105L;

	@Override
	protected void initStrategies(ApplicationContext context) {
		super.initStrategies(context);
	}
	
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		// IP地址
		//Global.ipThreadLocal.set(WebUtil.getIpAddress(request));
		
		ContextUtils.setRequest(request);
		ContextUtils.setResponse(response);
		
		super.service(request, response);
	}
}
