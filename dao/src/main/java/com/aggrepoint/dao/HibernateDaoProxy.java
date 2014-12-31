package com.aggrepoint.dao;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.cache.CacheManager;

import com.aggrepoint.dao.annotation.Cache;
import com.aggrepoint.dao.annotation.Delete;
import com.aggrepoint.dao.annotation.Find;
import com.aggrepoint.dao.annotation.Update;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 * 
 */
public class HibernateDaoProxy<T> implements InvocationHandler, Serializable {
	private static final long serialVersionUID = 1L;

	SessionFactory factory;
	CacheManager cacheManager;
	Class<T> clz;
	Hashtable<Method, HibernateDaoMethod<T>> htDaoMethods = new Hashtable<Method, HibernateDaoMethod<T>>();

	public HibernateDaoProxy(SessionFactory factory, CacheManager cacheManager,
			Class<?> daoInterface, Class<T> clz, List<IFunc> funcs) {
		this.factory = factory;
		this.cacheManager = cacheManager;
		this.clz = clz;

		for (Method method : daoInterface.getMethods()) {
			boolean found = false;

			for (Method m : HibernateDao.class.getMethods()) {
				if (method.equals(m)) {
					htDaoMethods.put(method, new HibernateDaoBaseMethod<T>(clz,
							method, factory));
					found = true;
					break;
				}
			}

			if (!found)
				for (Annotation ann : method.getDeclaredAnnotations()) {
					Class<?> t = ann.annotationType();
					if (t == Find.class || t == Cache.class
							|| t == Update.class || t == Delete.class) {
						htDaoMethods.put(method,
								new HibernateDaoAnnotationMethod<T>(method,
										ann, funcs, factory, cacheManager));
						found = true;
						break;
					}
				}

			if (!found)
				throw new IllegalArgumentException("Unsupported method "
						+ daoInterface.getName() + "." + method.getName() + ".");
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		HibernateDaoMethod<T> hdm = htDaoMethods.get(method);

		if (hdm != null)
			return hdm.invoke(proxy, method, args);

		return null;
	}
}
