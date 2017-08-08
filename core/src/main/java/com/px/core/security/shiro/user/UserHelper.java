package com.px.core.security.shiro.user;

import com.px.core.security.UserProfile;

public class UserHelper {

	public static <T> T to(UserProfile user){
		return (T) user;
	}
}