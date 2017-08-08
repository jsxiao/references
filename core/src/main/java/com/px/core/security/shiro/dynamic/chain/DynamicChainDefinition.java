package com.px.core.security.shiro.dynamic.chain ;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;

import com.px.core.security.shiro.dynamic.IDynamicAuthorizationService;
import com.px.core.security.shiro.dynamic.RolePath;

/**
 * 动态shiro配置
 * 
 * security-context.xml配置方式
 * 	
 * <bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
		<property name="securityManager" ref="securityManager" />
		<property name="loginUrl" value="/login.do" />
		<property name="successUrl" value="/" />
		<property name="unauthorizedUrl" value="/unauthor.jsp"/>
		<property name="filters">
	     	<util:map>
	     		<entry key="anyroles" value-ref="anyroleFilter"/>
	     	</util:map>
  		 </property>
		<property name="filterChainDefinitionMap" ref="dynamicChainDefinition"/>
	</bean>
	
	<bean id="dynamicChainDefinition" class="com.px.core.security.shiro.dynamic.chain.DynamicChainDefinition">  
	   <property name="filterChainDefinitions">
			<value>
				/resources/** = anon
				/login.do* = anon
				/comm/** = anon
				/logout.do* = logout 
				/auth/queryAllRole.do = roles[admin,good]
				/** = authc
		 	</value>
		</property>
		<property name = "dynamicPermissionService" ref= "myDynamicPermissionService"/>
	</bean> 
	
	<bean id="anyroleFilter" class="com.px.core.security.shiro.dynamic.AnyOfRolesAuthorizationFilter"/>
 * 
 * @author lionel
 *
 */
public class DynamicChainDefinition implements FactoryBean<Ini.Section>{  
  
 
	private static final Logger logger = LoggerFactory.getLogger(DynamicChainDefinition.class);

	private static final String allMathPrefix = "/**";
	
	private static final String anyroles = "anyroles[%s]";
	
	
	private IDynamicAuthorizationService dynamicAuthorizationService ; 
  
    private String filterChainDefinitions;  
  
    public Section getObject() throws BeansException {  
  
        Ini.Section section = getConfiguredFilterChainDefinitions();  
       
        //循环Resource的url,逐个添加到section中。section就是filterChainDefinitionMap,  
        //里面的键就是链接URL,值就是存在什么条件才能访问该链接  
        if(dynamicAuthorizationService != null){
        	String allMatchPath = section.get(allMathPrefix);
        	section.remove(allMathPrefix);
        	
        	
        	Map<String, String> dynamicPathMapping = getPathMapping( dynamicAuthorizationService.getDynamicPermission() ) ;
	        
        	if(logger.isDebugEnabled()){
        		logger.debug("动态权限验证列表： {}",dynamicPathMapping);
        	}
        	
        	if(dynamicPathMapping != null ){
        		section.putAll( dynamicPathMapping );
        	}
        	
	        if(allMatchPath != null){
	        	section.put(allMathPrefix, allMatchPath);
	        }
        }
  
        return section;  
    }  
  
    /**
     * 
     * @param dynamicPermission
     * @return
     */
    private Map<String, String> getPathMapping(List<RolePath> dynamicPermission) {
    	if(dynamicPermission != null && dynamicPermission.size() > 0 ){
    		Map<String, String>  pathMapping = new HashMap<String,String>();
    		for(RolePath rolePath : dynamicPermission){
    			if(pathMapping.containsKey(rolePath.getPath())){
    				String mapping = pathMapping.get(rolePath.getPath());
    				//use string instead of stringbuffer because of this only occurs when change roles which is rarelly executed ; 
    				mapping = mapping.substring(0, mapping.length() - 1 ) + "," + rolePath.getRoleName() + "]";
    				pathMapping.put(rolePath.getPath(), mapping);
    			}else{
    				pathMapping.put(rolePath.getPath(), String.format(anyroles,rolePath.getRoleName()));
    			}
    		}
    		return pathMapping ; 
    	}else{
    		return null;
    	}
	}

	/** 
     * 通过filterChainDefinitions对默认的url过滤定义 
     *  
     * @param filterChainDefinitions 默认的url过滤定义 
     */  
    public void setFilterChainDefinitions(String filterChainDefinitions) {  
        this.filterChainDefinitions = filterChainDefinitions;  
    }  
  
    public Class<?> getObjectType() {  
        return this.getClass();  
    }  
  
    public boolean isSingleton() {  
        return false;  
    }  
    
    /**
     * 
     * 获取xml配置的权限配置
     * 
     * @return
     */
    public Section getConfiguredFilterChainDefinitions(){
    	
    	 Ini ini = new Ini();  
         //加载默认的url  
         ini.load(filterChainDefinitions);  
         Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);  
    	 return section ;
    }

	public IDynamicAuthorizationService getDynamicAuthorizationService() {
		return dynamicAuthorizationService;
	}

	public void setDynamicAuthorizationService(IDynamicAuthorizationService dynamicAuthorizationService) {
		this.dynamicAuthorizationService = dynamicAuthorizationService;
	}

	
    
  
} 
