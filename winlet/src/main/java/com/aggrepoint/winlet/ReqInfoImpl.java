package com.aggrepoint.winlet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.aggrepoint.winlet.form.Form;
import com.aggrepoint.winlet.form.FormImpl;
import com.aggrepoint.winlet.spring.def.ReturnDef;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 */
public class ReqInfoImpl implements ReqConst, ReqInfo {
	private static long REQUEST_ID = 0;

	private long requestId;

	private HttpServletRequest request;
	// 遇到当执行了一定逻辑后，request.getSession(true)会不定期返回null
	private HttpSession session;
	private String path;
	private long winId;
	private String pageId;
	private String pageUrl;
	private long viewId;
	private String actionId;
	private String winRes;
	private String validateFieldName;
	private String validateFieldValue;
	private String validateFieldId;
	private boolean pageRefresh;
	private ViewInstance vi;
	private Form form;
	private PageStorage ps;
	private SharedPageStorage sps;
	private ReturnDef rd;

	// 待移植
	public boolean m_bUseAjax = true;

	/**
	 * 用于分解Action或Resource
	 */
	Pattern P_DECODE = Pattern.compile("([^!]*)!([^!]*)!([^!]+)(?:!(.*))?");

	public ReqInfoImpl(HttpServletRequest request, String path) {
		this.request = request;
		this.session = request.getSession(true);
		this.path = request.getContextPath() + path;

		requestId = REQUEST_ID++;

		pageId = getParameter(PARAM_PAGE_PATH, null);
		if (pageId == null)
			pageId = request.getRequestURI();

		pageUrl = getParameter(PARAM_PAGE_URL, null);
		if (pageUrl == null)
			pageUrl = request.getRequestURL().toString();

		winId = getParameter(PARAM_WIN_ID, 0l);
		String vid = request
				.getHeader(WinletConst.REQUEST_HEADER_VIEW_HEADER_ID);
		if (vid == null || vid.equals(""))
			vid = getParameter(PARAM_WIN_VIEW, "");
		if ("".equals(vid))
			viewId = winId;
		else
			viewId = Long.parseLong(vid);

		actionId = getParameter(PARAM_WIN_ACTION, null);
		if (actionId != null) {
			Matcher m;
			synchronized (P_DECODE) {
				m = P_DECODE.matcher(actionId);
			}

			if (m.find()) {
				try {
					winId = Long.parseLong(m.group(1));
					viewId = Long.parseLong(m.group(2));
				} catch (Exception e) {
				}
				actionId = m.group(3);
			}
		} else {
			pageRefresh = "yes".equalsIgnoreCase(getParameter(
					PARAM_PAGE_REFRESH, ""));
		}

		validateFieldName = getParameter(PARAM_WIN_VALIDATE_FIELD, null);
		if (validateFieldName != null) {
			validateFieldValue = getParameter(PARAM_WIN_VALIDATE_FIELD_VALUE,
					"");
			validateFieldId = getParameter(PARAM_WIN_VALIDATE_FIELD_ID, "");
		}

		form = new FormImpl(this);

		winRes = getParameter(PARAM_WIN_RES, null);
		if (winRes != null) {
			Matcher m;
			synchronized (P_DECODE) {
				m = P_DECODE.matcher(winRes);
			}
			if (m.find()) {
				try {
					winId = Long.parseLong(m.group(1));
					viewId = Long.parseLong(m.group(2));
				} catch (Exception e) {
				}
				winRes = m.group(3);
			}
		}

		ContextUtils.setReqInfo(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getParameter(javax.servlet.http.
	 * HttpServletRequest, java.lang.String, java.lang.String)
	 */
	@Override
	public String getParameter(String name, String def) {
		String str;

		str = request.getParameter(name);
		if (str == null)
			return def;
		return str.trim();
	}

	@Override
	public int getParameter(String name, int def) {
		return Integer.parseInt(getParameter(name, Integer.toString(def)));
	}

	@Override
	public long getParameter(String name, long def) {
		return Long.parseLong(getParameter(name, Long.toString(def)));
	}

	public void setViewInstance(ViewInstance vi) {
		this.vi = vi;
	}

	public void setReturnDef(ReturnDef rd) {
		this.rd = rd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getRequest()
	 */
	@Override
	public HttpServletRequest getRequest() {
		return request;
	}

	public String getRequestPath() {
		String str = request
				.getHeader(WinletConst.REQUEST_HEADER_VIEW_HEADER_REQ_PATH);
		if (str == null || str.equals("")) {
			str = request.getRequestURI();
			int idx = str.indexOf("/", 1);
			str = str.substring(idx);
		}
		return str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getSession()
	 */
	@Override
	public HttpSession getSession() {
		return session;
	}

	@Override
	public UserProfile getUser() {
		return ContextUtils.getUser(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getPath()
	 */
	@Override
	public String getPath() {
		return path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getRequestId()
	 */
	@Override
	public long getRequestId() {
		return requestId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getWinId()
	 */
	@Override
	public long getWinId() {
		return winId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getPageId()
	 */
	@Override
	public String getPageId() {
		return pageId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getPageUrl()
	 */
	@Override
	public String getPageUrl() {
		return pageUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getViewId()
	 */
	@Override
	public long getViewId() {
		return viewId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getActionId()
	 */
	@Override
	public String getActionId() {
		return actionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getFormName()
	 */
	@Override
	public Form getForm() {
		return form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#isValidateField()
	 */
	@Override
	public boolean isValidateField() {
		return validateFieldName != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getValidateFieldName()
	 */
	@Override
	public String getValidateFieldName() {
		return validateFieldName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getValidateFieldValue()
	 */
	@Override
	public String getValidateFieldValue() {
		return validateFieldValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getValidateFieldId()
	 */
	@Override
	public String getValidateFieldId() {
		return validateFieldId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#isPageRefresh()
	 */
	@Override
	public boolean isPageRefresh() {
		return pageRefresh;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getViewInstance()
	 */
	@Override
	public ViewInstance getViewInstance() {
		return vi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getPageStorage()
	 */
	@Override
	public PageStorage getPageStorage() {
		if (ps == null)
			ps = new PageStorageImpl(this);
		return ps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getPageStorage()
	 */
	@Override
	public SharedPageStorage getSharedPageStorage() {
		if (sps == null)
			sps = new SharedPageStorageImpl(this);
		return sps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aggrepoint.winlet.ReqInfo#getReturnDef()
	 */
	@Override
	public ReturnDef getReturnDef() {
		return rd;
	}
}
