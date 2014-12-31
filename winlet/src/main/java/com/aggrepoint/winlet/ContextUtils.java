package com.aggrepoint.winlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @see ReqInfoImpl
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 */
public class ContextUtils {
	public static final String REQUEST_ATTR_REQUEST = ContextUtils.class
			.getName() + "REQ_INFO";

	private static String REQUEST_USER_ENGINE = ContextUtils.class.getName()
			+ ".REQUEST_USER_ENGINE";
	private static String REQUEST_RULE_ENGINE = ContextUtils.class.getName()
			+ ".REQUEST_RULE_ENGINE";
	private static String REQUEST_PSN_RULE_ENGINE = ContextUtils.class
			.getName() + ".REQUEST_PSN_RULE_ENGINE";
	private static String REQUEST_CONFIG_PROVIDER = ContextUtils.class
			.getName() + ".REQUEST_CONFIG_PROVIDER";
	private static String REQUEST_CODE_MAP_PROVIDER = ContextUtils.class
			.getName() + ".REQUEST_CODE_TABLE_PROVIDER";
	private static String REQUEST_LOGINFO_KEY = LogInfoImpl.class.getName()
			+ ".REQUEST_LOGINFO_KEY";

	public static ReqInfoImpl getReqInfo() {
		return (ReqInfoImpl) RequestContextHolder.currentRequestAttributes()
				.getAttribute(REQUEST_ATTR_REQUEST,
						RequestAttributes.SCOPE_REQUEST);
	}

	public static void setReqInfo(ReqInfo reqInfo) {
		RequestContextHolder.currentRequestAttributes().setAttribute(
				REQUEST_ATTR_REQUEST, reqInfo, RequestAttributes.SCOPE_REQUEST);
	}

	public static HttpServletRequest getRequest() {
		return getReqInfo().getRequest();
	}

	public static Object getRequestAttribute(String name) {
		return getRequest().getAttribute(name);
	}

	public static void setRequestAttribute(String name, Object value) {
		getRequest().setAttribute(name, value);
	}

	public static void removeRequestAttribute(String name) {
		getRequest().removeAttribute(name);
	}

	public static HttpSession getSession() {
		return getReqInfo().getSession();
	}

	public static Object getSessionAttribute(String name) {
		return getSession().getAttribute(name);
	}

	public static void setSessionAttribute(String name, Object value) {
		getSession().setAttribute(name, value);
	}

	public static void removeSessionAttribute(String name) {
		getSession().removeAttribute(name);
	}

	public static LogInfoImpl getLogInfo(HttpServletRequest request) {
		return (LogInfoImpl) request.getAttribute(REQUEST_LOGINFO_KEY);
	}

	public static void setLogInfo(HttpServletRequest request, LogInfo li) {
		request.setAttribute(REQUEST_LOGINFO_KEY, li);
	}

	public static UserEngine getUserEngine(HttpServletRequest request) {
		return (UserEngine) request.getAttribute(REQUEST_USER_ENGINE);
	}

	public static UserProfile getUser(HttpServletRequest request) {
		return ((UserEngine) request.getAttribute(REQUEST_USER_ENGINE))
				.getUser(request);
	}

	public static void setUserEngine(HttpServletRequest request,
			UserEngine userEngine) {
		request.setAttribute(REQUEST_USER_ENGINE, userEngine);
	}

	public static AccessRuleEngine getAccessRuleEngine(
			HttpServletRequest request) {
		return (AccessRuleEngine) request.getAttribute(REQUEST_RULE_ENGINE);
	}

	public static void setAccessRuleEngine(HttpServletRequest request,
			AccessRuleEngine ruleEngine) {
		request.setAttribute(REQUEST_RULE_ENGINE, ruleEngine);
	}

	public static PsnRuleEngine getPsnRuleEngine(HttpServletRequest request) {
		return (PsnRuleEngine) request.getAttribute(REQUEST_PSN_RULE_ENGINE);
	}

	public static void setPsnRuleEngine(HttpServletRequest request,
			PsnRuleEngine ruleEngine) {
		request.setAttribute(REQUEST_PSN_RULE_ENGINE, ruleEngine);
	}

	public static ConfigProvider getConfigProvider(HttpServletRequest request) {
		return (ConfigProvider) request.getAttribute(REQUEST_CONFIG_PROVIDER);
	}

	public static void setConfigProvider(HttpServletRequest request,
			ConfigProvider provider) {
		request.setAttribute(REQUEST_CONFIG_PROVIDER, provider);
	}

	public static CodeMapProvider getCodeMapProvider(HttpServletRequest request) {
		return (CodeMapProvider) request
				.getAttribute(REQUEST_CODE_MAP_PROVIDER);
	}

	public static void setCodeMapProvider(HttpServletRequest request,
			CodeMapProvider provider) {
		request.setAttribute(REQUEST_CODE_MAP_PROVIDER, provider);
	}
}
