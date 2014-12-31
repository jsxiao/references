package com.aggrepoint.winlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.aggrepoint.winlet.form.Form;
import com.aggrepoint.winlet.spring.def.ReturnDef;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 */
public interface ReqInfo {
	public abstract String getParameter(String name, String def);

	public abstract int getParameter(String name, int def);

	public abstract long getParameter(String name, long def);

	public abstract HttpServletRequest getRequest();

	public abstract String getRequestPath();

	public abstract HttpSession getSession();

	public abstract UserProfile getUser();

	public abstract String getPath();

	public abstract long getRequestId();

	public abstract long getWinId();

	public abstract String getPageId();

	public abstract String getPageUrl();

	public abstract long getViewId();

	public abstract String getActionId();

	public abstract Form getForm();

	public abstract boolean isValidateField();

	public abstract String getValidateFieldName();

	public abstract String getValidateFieldValue();

	public abstract String getValidateFieldId();

	public abstract boolean isPageRefresh();

	public abstract ViewInstance getViewInstance();

	public abstract PageStorage getPageStorage();

	public abstract SharedPageStorage getSharedPageStorage();

	public abstract ReturnDef getReturnDef();

}