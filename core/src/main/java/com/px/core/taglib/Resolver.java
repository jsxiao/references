package com.px.core.taglib;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

import com.px.core.security.shiro.user.UserContext;

/**
 * 
 * @author jason
 *
 */
public class Resolver extends javax.el.ELResolver implements
		ServletContextListener {

	@Override
	public Class<?> getCommonPropertyType(ELContext arg0, Object arg1) {
		return null;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext arg0,
			Object arg1) {
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property)
			throws NullPointerException, PropertyNotFoundException, ELException {
		return null;
	}

	@Override
	public Object getValue(ELContext context, Object base, Object property)
			throws NullPointerException, PropertyNotFoundException, ELException {
		Object val = null;
		
		if(base == null){
			if(property.equals("u"))
				val = UserContext.getUser();
			
			if(val != null)
				context.setPropertyResolved(true);
		}
			
		
		//System.out.println(ReflectionToStringBuilder.toString(base));
		/*if (property.equals("u")) {
			val = ContextUtils.getUserContext().getUser();
			System.out.println("--"+ContextUtils.getUserContext().getUser().getLoginName());
		}*/
		
		/*if (base == null) {
			if (property.equals("u")) {
				val = ContextUtils.getUserContext().getUser();
			} else if (property.equals("c")) {
				/*val = ContextUtils
						.getCodeMapProvider(ContextUtils.getRequest());
			} else if (property.equals("f")) {
				// val = ThreadContext.getAttribute(THREAD_ATTR_REQUEST);
			} else if (property.equals("e")) {
				// val = ThreadContext.getAttribute(THREAD_ATTR_EXCEPTION);
			} else if (property.equals("cin"))
				val = new CinletEl();
			// else if (property.equals("rinfo"))
			// val = WinletReqInfo.getInfo((IModuleRequest) ThreadContext
			// .getAttribute(THREAD_ATTR_REQUEST));

			if (val != null)
				context.setPropertyResolved(true);
		} else {
			if (base instanceof CodeMapProvider) {
				//val = ((CodeMapProvider) base).getMap(property.toString());
				context.setPropertyResolved(true);
			} else if (base instanceof CinletEl) {
				CinletEl winEl = (CinletEl) base;
				if (winEl.getMethod() == null) {
					winEl.setMethod(property.toString());
					val = winEl;
				} else {
					val = winEl.execute(property.toString());
					context.setPropertyResolved(true);
				}
			} else if (base instanceof CodeMapWrapper) {
				val = ((CodeMapWrapper) base).get(property.toString());
				context.setPropertyResolved(true);
			}
		}*/
		return val;
	}

	@Override
	public boolean isReadOnly(ELContext arg0, Object arg1, Object arg2)
			throws NullPointerException, PropertyNotFoundException, ELException {
		return false;
	}

	@Override
	public void setValue(ELContext arg0, Object arg1, Object arg2, Object arg3)
			throws NullPointerException, PropertyNotFoundException,
			PropertyNotWritableException, ELException {
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent evt) {
		ServletContext context = evt.getServletContext();
		JspApplicationContext jspContext = JspFactory.getDefaultFactory()
				.getJspApplicationContext(context);
		jspContext.addELResolver(new Resolver());
	}
}
