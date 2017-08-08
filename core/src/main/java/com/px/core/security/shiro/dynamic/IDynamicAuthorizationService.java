package com.px.core.security.shiro.dynamic;

import java.util.List;

/**
 * com.shiro
 * functional describe:动态权限配置Service
 *
 * @author DR.YangLong [410357434@163.com] lionel
 * @version 1.0 2015/1/17 10:33
 */
public interface IDynamicAuthorizationService {
    
	/**
	 * 
	 * @return 角色路径的集合
	 */
	List<RolePath>  getDynamicPermission();

}
