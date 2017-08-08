package com.px.core.security.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter{


	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		
		String username = getUsername(request);
		String password = getPassword(request);
		
		if(password == null)
			password = "";
		
		String host = getHost(request);
		
		return new UsernamePasswordToken(username, password, host);
	}
}
