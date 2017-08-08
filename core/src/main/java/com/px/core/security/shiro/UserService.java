package com.px.core.security.shiro;

import java.util.Set;

import com.px.core.security.UserProfile;

/**
 * 
 * @author jason, lionel
 *
 * @param <T> 用户唯一标识
 */
public interface UserService<T> {
	
	/**
	 * 获取用户信息
	 * @param username
	 * @return
	 */
	public UserProfile loadUser(String username);
	
	/**
	 * 获取用户信息
	 * @param username
	 * @param findType
	 * @return
	 */
	public UserProfile loadUser(String username, FindType findType);
	
	
	/**
	 * 获取用户信息
	 * @param id
	 * @return
	 */
	public UserProfile getUserDetails(T id);
	
	/**
	 * 获取用户角色
	 */
	/**
	 * 获取用户的角色
	 * @param id
	 * @return
	 */
	public Set<String> getUserRoles(T id);

	
	/**
	 * 获取用户的权限 【角色与权限的区别，角色是权限的集合或组合】
	 * @param id
	 * @return
	 */
	public Set<String> getStringPermissions(T id);
	
	
	
	
}
