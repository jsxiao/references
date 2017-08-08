package com.px.core.security.shiro.user;

import org.apache.shiro.SecurityUtils;

import com.px.core.security.UserProfile;

/**
 * 警告: 该对象的任何方法都不对外开放, 请勿私自调用(如果你不了解运行机制).
 * @author Jason
 *
 */
public class UserContext {

	private static final UserProfile ANONYMOUS = new UserProfile(){

		@Override
		public String getPassword() {
			return "";
		}

		@Override
		public String getUserName() {
			return "";
		}

		@Override
		public Integer getId() {
			return null;
		}

		@Override
		public String getRealName() {
			return null;
		}

		@Override
		public String getIdCard() {
			return null;
		}

		@Override
		public boolean isAnonymous() {
			return true;
		}

		@Override
		public boolean isReal() {
			return false;
		}};
	
	/**
	 * 1. 该方法不应该出现在Service中 <br>
	 * 2. 不应当手动调用. 如果需要当前登录用户请在Controller方法参数中注入UserProfile
	 * @return
	 */
	public static UserProfile getUser(){
		Object principal = SecurityUtils.getSubject().getPrincipal();
		if(principal == null) 
			return ANONYMOUS;
		else
			return (UserProfile) principal;
	}
	
	public static UserProfile getAnonymousUser(){
		return ANONYMOUS;
	}
	
	public static void logout(){
		SecurityUtils.getSubject().logout();
	}
}
