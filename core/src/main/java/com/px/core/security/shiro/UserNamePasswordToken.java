package com.px.core.security.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UserNamePasswordToken extends UsernamePasswordToken{
	private static final long serialVersionUID = 1L;
	
	private FindType findType;
	
	public UserNamePasswordToken(String username, String password, FindType findType) {
		super(username, password);
		this.findType = findType;
	}
	
	public FindType getFindType() {
		return findType;
	}
}
