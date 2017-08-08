package com.px.core.security.shiro;

import java.io.Serializable;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.px.core.security.UserProfile;

public class SysAuthorizingRealm extends AuthorizingRealm implements InitializingBean, ApplicationContextAware,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6944136874466133349L;

	private static Logger logger = LoggerFactory.getLogger(SysAuthorizingRealm.class);
	
	private  ApplicationContext ctx;
	
	@SuppressWarnings("rawtypes")
	private  UserService authService;
	
	/**
	 * 获取权限
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
	
		if(logger.isDebugEnabled()){
			logger.debug("获取授权验证");
		}
		
		final UserProfile userProfile = (UserProfile)principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.setRoles(authService.getUserRoles(userProfile.getId()));
		simpleAuthorizationInfo.setStringPermissions(authService.getStringPermissions(userProfile.getId()));
		return simpleAuthorizationInfo;
	}

	/**
	 * 登陆认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		UserNamePasswordToken upt = (UserNamePasswordToken) token;
		UserProfile user = authService.loadUser(upt.getUsername(), upt.getFindType());
		if(user != null){
			return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		}
		return null;
	}
	
	public void initCredentialsMatcher(){
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Md5Hash.ALGORITHM_NAME);
		setCredentialsMatcher(matcher);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(authService == null){
			logger.error("没有找到 [com.px.core.security.shiro.UserService] 的实现类");
			throw new ShiroException("没有找到 [com.px.core.security.shiro.UserService] 的实现类");
		}
		
		initCredentialsMatcher();
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.ctx = arg0;
		authService = this.ctx.getBean(UserService.class);
	}
	
	
}
