package com.aggrepoint.winlet;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 */
public interface WinletConst {
	public static final int WINLET_SCOPE = 15;

	// /////////////////////////////////////////////////
	//
	// Servlet Request属性
	//
	// /////////////////////////////////////////////////
	public static final String REQUEST_ATTR_WINLET = "com.aggrepoint.winlet.winlet";

	// /////////////////////////////////////////////////
	//
	// 嵌套视图请求头
	//
	// /////////////////////////////////////////////////

	/** 嵌套视图请求头：视图ID */
	public static final String REQUEST_HEADER_VIEW_HEADER_ID = "com.aggrepoint.winlet.view.id";
	/** 嵌套视图请求头：RequestPath */
	public static final String REQUEST_HEADER_VIEW_HEADER_REQ_PATH = "com.aggrepoint.winlet.req.path";
}
