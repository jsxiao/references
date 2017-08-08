package com.px.core.security.shiro.dynamic;

import java.util.Map;

import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.px.core.security.shiro.dynamic.chain.DynamicChainDefinition;

/**
 * functional describe:shiro动态权限管理，<br/>
 * 配置权限时，尽量不要使用/**来配置（重置时将被清除，如果要使用，在{@link JdbcPermissionDao}实现类中最后添加key="/**"，value="anon"），每个链接都应该配置独立的权限信息<br/>
 *
 * @author 410357434@163.com lionel
 * @version 1.0 2015/1/17 11:10
 */
public class DynamicShiroFilterManager implements ApplicationContextAware {
   
	private static final Logger logger = LoggerFactory.getLogger(DynamicShiroFilterManager.class);

	private static DynamicShiroFilterManager dynamicShiroFilterManager;
	
    private AbstractShiroFilter shiroFilter;
    
    private DynamicChainDefinition   dynamicChainDefinition;

   
    private DefaultFilterChainManager getFilterChainManager() throws Exception {
        // 获取过滤管理器
        PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                .getFilterChainResolver();
        DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
        return manager;
    }

    private void addToChain(DefaultFilterChainManager manager, Map<String, String> definitions) throws Exception {
    	
    	//clear all filter chain ; 
    	manager.getFilterChains().clear();

    	if (definitions == null || CollectionUtils.isEmpty(definitions)) {
            return;
        }
        
    	for (Map.Entry<String, String> entry : definitions.entrySet()) {
            String url = entry.getKey();
            String chainDefinition = entry.getValue().trim().replace(" ", "");
            manager.createChain(url, chainDefinition);
        }
    }

    public synchronized void updatePermission() {
        logger.debug("更新资源配置开始！");
        try {
            // 获取和清空初始权限配置
            DefaultFilterChainManager manager = getFilterChainManager();
            addToChain(manager, dynamicChainDefinition.getObject());
            logger.debug("更新资源权限配置成功。");
        } catch (Exception e) {
            logger.error("更新资源权限配置发生错误!", e);
        }
    }
    
   
	
	
	public static void  update(){
		dynamicShiroFilterManager.updatePermission();
	}
	
	

	public AbstractShiroFilter getShiroFilter() {
		return shiroFilter;
	}

	public void setShiroFilter(AbstractShiroFilter shiroFilter) {
		this.shiroFilter = shiroFilter;
	}

	public DynamicChainDefinition getDynamicChainDefinition() {
		return dynamicChainDefinition;
	}

	public void setDynamicChainDefinition(DynamicChainDefinition dynamicChainDefinition) {
		this.dynamicChainDefinition = dynamicChainDefinition;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		 shiroFilter = applicationContext.getBean(AbstractShiroFilter.class);
		 dynamicChainDefinition = (DynamicChainDefinition) applicationContext.getBean("&dynamicChainDefinition");
		 dynamicShiroFilterManager = this ; 
	}

	
}