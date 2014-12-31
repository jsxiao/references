package com.aggrepoint.winlet.taglib;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.aggrepoint.winlet.Context;
import com.aggrepoint.winlet.ContextUtils;
import com.aggrepoint.winlet.ReqInfo;
import com.aggrepoint.winlet.ViewInstance;
import com.aggrepoint.winlet.WinletConst;
import com.aggrepoint.winlet.WinletManager;
import com.aggrepoint.winlet.spring.WinletClassLoader;
import com.aggrepoint.winlet.spring.def.WinletDef;
import com.aggrepoint.winlet.utils.BufferedResponseWrapper;
import com.aggrepoint.winlet.utils.TypeCast;

/**
 * @author Jiangming Yang (yangjm@gmail.com)
 */
public class IncludeTag extends BodyTagSupport implements WinletConst {
	private static final long serialVersionUID = 1L;

	String var;

	String vars;

	String m_strView;

	WinletDef winlet;

	Hashtable<String, String> m_params;

	public void setVar(String var) {
		this.var = var;
	}

	public void setVars(String var) {
		this.vars = var;
	}

	public void setView(String view) {
		m_strView = view;
	}

	public void setWinlet(String winlet) {
		Class<?> clz = WinletClassLoader.getWinletClassByPath(winlet);
		this.winlet = WinletDef.getDef(clz);
	}

	/**
	 * 跟踪Tomcat代码发现Tomcat在include或forward过程中使用很多Request值，因此
	 * 不能简单的模拟一个全新的request环境。<br>
	 * 
	 * 虽然目前RequestWrapper没有实现任何功能，保留它以便需要时添加
	 * 
	 * @author Jiangming Yang
	 * 
	 */
	public static class RequestWrapper extends HttpServletRequestWrapper {
		long viewId;
		String requestPath;
		Hashtable<String, String> m_params;

		public RequestWrapper(HttpServletRequest request, long viewId,
				String requestPath, Hashtable<String, String> params) {
			super(request);
			this.viewId = viewId;
			this.requestPath = requestPath;
			this.m_params = params;
		}

		public String getHeader(String name) {
			if (name.equals(REQUEST_HEADER_VIEW_HEADER_ID))
				return Long.toString(viewId);
			if (name.equals(REQUEST_HEADER_VIEW_HEADER_REQ_PATH))
				return requestPath;
			return super.getHeader(name);
		}

		public String getParameter(String name) {
			String param = m_params.get(name);
			return param == null ? super.getParameter(name) : param;
		}

		@SuppressWarnings("rawtypes")
		public Enumeration getParameterNames() {
			if (m_params.size() == 0)
				return super.getParameterNames();

			Vector<String> set = new Vector<String>();

			set.addAll(m_params.keySet());
			for (Enumeration e = super.getParameterNames(); e.hasMoreElements();)
				set.add((String) e.nextElement());

			return set.elements();
		}

		public String[] getParameterValues(String name) {
			String param = m_params.get(name);
			return param == null ? super.getParameterValues(name)
					: new String[] { param };
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Map getParameterMap() {
			Map map = super.getParameterMap();
			for (String key : m_params.keySet())
				map.put(key, m_params.get(key));
			return map;
		}
	}

	@Override
	public int doStartTag() {
		m_params = new Hashtable<String, String>();
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doAfterBody() {
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspTagException {
		try {
			ReqInfo ri = ContextUtils.getReqInfo();

			ViewInstance view = ri.getViewInstance();

			ViewInstance newView = view.addSub(winlet == null ? null
					: WinletManager.getWinlet(Context.get(), ri, winlet),
					m_strView, null);
			newView.setParams(m_params);

			RequestWrapper request = new RequestWrapper(
					(HttpServletRequest) pageContext.getRequest(),
					newView.getId(), ri.getRequestPath(), m_params);

			BufferedResponseWrapper response = new BufferedResponseWrapper(
					(HttpServletResponse) pageContext.getResponse());

			// 注：
			// 这里使用forward而不是include，因为使用include的情况下在被include对象
			// 中使用getRequestURI()等方法获得的是当前的URI而不是被include对象的URI，
			// 因此ADK无法正确判断被include的资源。使用forward则不存在这个问题。因为已经
			// 使用了responseWrapper，因此用forward也是可行的。
			pageContext.getServletContext()
					.getRequestDispatcher(ri.getRequestPath())
					.forward(request, response);
			byte[] bytes = response.getBuffered();

			ContextUtils.setReqInfo(ri);

			Vector<String> v = null;
			if (vars != null) {
				v = TypeCast.cast(pageContext.getAttribute(vars));
				if (v == null) {
					v = new Vector<String>();
					pageContext.setAttribute(vars, v);
				}
			}

			StringBuffer sb = new StringBuffer();
			sb.append("<span id=\"ap_view_").append(newView.getId())
					.append("\">");
			if (bytes != null)
				sb.append(new String(bytes, "UTF-8"));
			sb.append("</span>");

			String str = sb.toString();
			str = str.replaceAll("win\\$\\.post\\s*\\(",
					"win\\$._post(" + ri.getWinId() + ", " + newView.getId()
							+ ", ");
			str = str.replaceAll("win\\$\\.get\\s*\\(",
					"win\\$._get(" + ri.getWinId() + ", " + newView.getId()
							+ ", ");
			str = str.replaceAll("win\\$\\.url\\s*\\(",
					"win\\$._url(" + ri.getWinId() + ", " + newView.getId()
							+ ", ");
			str = str.replaceAll("win\\$\\.submit\\s*\\(", "win\\$._submit("
					+ ri.getWinId() + ", " + newView.getId() + ", ");

			if (v != null)
				v.add(str);
			else
				pageContext.setAttribute(var, str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException(e.getMessage());
		}

		return EVAL_PAGE;
	}
}
