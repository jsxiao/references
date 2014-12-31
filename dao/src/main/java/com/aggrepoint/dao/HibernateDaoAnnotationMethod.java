package com.aggrepoint.dao;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.DefaultKeyGenerator;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.core.annotation.AnnotationUtils;

import com.aggrepoint.dao.annotation.Cache;
import com.aggrepoint.dao.annotation.Delete;
import com.aggrepoint.dao.annotation.Find;
import com.aggrepoint.dao.annotation.Like;
import com.aggrepoint.dao.annotation.PageNum;
import com.aggrepoint.dao.annotation.PageSize;
import com.aggrepoint.dao.annotation.Param;
import com.aggrepoint.dao.annotation.Replace;
import com.aggrepoint.dao.annotation.Update;
import com.aggrepoint.jpa.UpdatedDate;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 */
public class HibernateDaoAnnotationMethod<T> implements HibernateDaoMethod<T> {
	static Pattern P_PART = Pattern.compile("#\\{(.+?)\\}");
	static Pattern P_NUM_PARAM = Pattern.compile(":(\\d+)");
	static Pattern P_PROP_PARAM = Pattern
			.compile(":([\\w\\d]+)\\.([\\w\\d\\.]+)");
	static final String GROUP_BY = " group by ";

	static final int TYPE_FIND = 0;
	static final int TYPE_CACHE = 1;
	static final int TYPE_UPDATE = 2;
	static final int TYPE_DELETE = 3;
	static final int TYPE_FIND_SQL = 4;
	static final int TYPE_CACHE_SQL = 5;
	static final int TYPE_UPDATE_SQL = 6;
	static final int TYPE_DELETE_SQL = 7;
	static final int RETURN_VOID = 0;
	static final int RETURN_LIST = 1;
	static final int RETURN_PAGE = 2;
	static final int RETURN_OBJECT = 3;

	static private KeyGenerator keyGenerator = new com.aggrepoint.dao.utils.KeyGenerator(); 
	
	SessionFactory factory;
	CacheManager cacheManager;
	Method method;
	Annotation[][] pans;
	int type;
	int retType;
	String hql;
	Cache cache;
	Class<?> entityClass;
	boolean useEntity;
	Column updateTimeCol;
	String updateTimeProp;
	Vector<QueryPart> parts = new Vector<QueryPart>();
	HashSet<String> params = new HashSet<String>();
	Hashtable<String, ParamProperty> props = new Hashtable<String, ParamProperty>();
	Hashtable<String, Like> likes = new Hashtable<String, Like>();
	Hashtable<String, Replace> replaces = new Hashtable<String, Replace>();
	Hashtable<String, Function> funcs = new Hashtable<String, Function>();
	Hashtable<String, Integer> positions = new Hashtable<String, Integer>();
	int pageNumIdx = -1;
	int pageSizeIdx = -1;

	public HibernateDaoAnnotationMethod(Method method, Annotation ann,
			List<IFunc> funcs, SessionFactory factory, CacheManager cacheManager) {
		this.factory = factory;
		this.cacheManager = cacheManager;
		this.method = method;

		if (ann.annotationType() == Find.class) {
			if (!"".equals(((Find) ann).sql())) {
				type = TYPE_FIND_SQL;
				hql = ((Find) ann).sql();

				entityClass = ((Find) ann).entity();
				if (entityClass == Object.class)
					entityClass = null;
			} else {
				type = TYPE_FIND;
				hql = ((Find) ann).value();
			}
		} else if (ann.annotationType() == Cache.class) {
			if (cacheManager == null)
				throw new IllegalArgumentException(
						"CacheManager must be configured for using Cache on "
								+ method.getDeclaringClass().getName() + "."
								+ method.getName());

			cache = (Cache) ann;
			entityClass = cache.entity();
			useEntity = cache.useEntity();
			if (entityClass == Object.class)
				throw new IllegalArgumentException(
						"Entity class not specified for Cache on "
								+ method.getDeclaringClass().getName() + "."
								+ method.getName());
			try {
				for (Method m : entityClass.getMethods()) {
					if (AnnotationUtils.findAnnotation(m, UpdatedDate.class) != null) {
						updateTimeCol = AnnotationUtils.findAnnotation(m,
								Column.class);
						if (updateTimeCol != null) {
							updateTimeProp = Introspector.decapitalize(m
									.getName().substring(3));
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (updateTimeCol == null)
				throw new IllegalArgumentException(
						"Unable to find a field annotated by both UpdatedDate and Column in entity class "
								+ entityClass.getName()
								+ " for CacheFind method "
								+ method.getDeclaringClass().getName()
								+ "."
								+ method.getName());

			if (!"".equals(((Cache) ann).sql())) {
				type = TYPE_CACHE_SQL;
				hql = ((Cache) ann).sql();
			} else {
				type = TYPE_CACHE;
				hql = ((Cache) ann).value();
			}

//			if (hql.toLowerCase().contains(GROUP_BY))
//				throw new IllegalArgumentException(
//						"HQL or SQL that contains GROUP BY isn't suppored by @Cache. Entity class: "
//								+ entityClass.getName() + ", method: "
//								+ method.getDeclaringClass().getName() + "."
//								+ method.getName());
		} else if (ann.annotationType() == Update.class) {
			if (!"".equals(((Update) ann).sql())) {
				type = TYPE_UPDATE_SQL;
				hql = ((Update) ann).sql();
			} else {
				type = TYPE_UPDATE;
				hql = ((Update) ann).value();
			}
		} else if (ann.annotationType() == Delete.class) {
			if (!"".equals(((Delete) ann).sql())) {
				type = TYPE_DELETE_SQL;
				hql = ((Delete) ann).sql();
			} else {
				type = TYPE_DELETE;
				hql = ((Delete) ann).value();
			}
		} else {
			throw new IllegalArgumentException("Unsupported annotation '" + ann
					+ "' on " + method.getDeclaringClass().getName() + "."
					+ method.getName());
		}

		Function[] functions = null;
		try {
			functions = Function.getFunctions(funcs, hql);
		} catch (FunctionNotFoundException e) {
			throw new IllegalArgumentException("Undefined function '"
					+ e.getName() + "' used by "
					+ method.getDeclaringClass().getName() + "."
					+ method.getName());
		}
		if (functions != null) {
			for (Function f : functions)
				this.funcs.put(f.getMatch(), f);

			// 为避免替换数字参数时影响到function的参数，先将function转换为特殊的字符串
			for (int i = 0; i < functions.length; i++)
				hql = hql.replaceAll(Pattern.quote(functions[i].match), "!!!!!"
						+ i + "!!!!!");
		}

		// 替换数字参数
		hql = P_NUM_PARAM.matcher(hql).replaceAll(":p_$1");

		if (functions != null) {
			// 恢复function
			for (int i = 0; i < functions.length; i++)
				hql = hql.replaceAll(Pattern.quote("!!!!!" + i + "!!!!!"),
						functions[i].match);
		}

		pans = method.getParameterAnnotations();
		for (int i = 0; i < pans.length; i++) {
			if (pans[i].length == 0) {
				String name = "p_" + Integer.toString(i + 1);

				if (positions.containsKey(name))
					throw new IllegalArgumentException(
							"Duplicated param/like/replace name: '" + name
									+ "' on "
									+ method.getDeclaringClass().getName()
									+ "." + method.getName());

				params.add(name);
				positions.put(name, i);
			} else {
				for (int j = 0; j < pans[i].length; j++)
					if (pans[i][j].annotationType() == Param.class) {
						Param p = (Param) pans[i][j];

						String name = p.value();
						if (name.equals(""))
							name = "p_" + Integer.toString(i + 1);

						if (positions.containsKey(name))
							throw new IllegalArgumentException(
									"Duplicated param/like/replace name: '"
											+ name
											+ "' on "
											+ method.getDeclaringClass()
													.getName() + "."
											+ method.getName());

						params.add(name);
						positions.put(name, i);
						break;
					} else if (pans[i][j].annotationType() == Like.class) {
						Like p = (Like) pans[i][j];

						String name = p.value();
						if (name.equals(""))
							name = "p_" + Integer.toString(i + 1);

						if (positions.containsKey(name))
							throw new IllegalArgumentException(
									"Duplicated param/like/replace name: '"
											+ name
											+ "' on "
											+ method.getDeclaringClass()
													.getName() + "."
											+ method.getName());

						likes.put(name, p);
						positions.put(name, i);
						break;
					} else if (pans[i][j].annotationType() == Replace.class) {
						Replace r = (Replace) pans[i][j];

						String name = r.value();
						if (name.equals(""))
							name = "p_" + Integer.toString(i + 1);

						if (positions.containsKey(name))
							throw new IllegalArgumentException(
									"Duplicated param/like/replace name: '"
											+ name
											+ "' on "
											+ method.getDeclaringClass()
													.getName() + "."
											+ method.getName());

						replaces.put(name, r);
						positions.put(name, i);
						break;
					} else if (pans[i][j].annotationType() == PageNum.class) {
						if (type != TYPE_FIND && type != TYPE_CACHE
								&& type != TYPE_FIND_SQL && type != TYPE_CACHE_SQL)
							throw new IllegalArgumentException(
									"@PageNum shouldn't be used on "
											+ method.getDeclaringClass()
													.getName() + "."
											+ method.getName());
						pageNumIdx = i;
						break;
					} else if (pans[i][j].annotationType() == PageSize.class) {
						if (type != TYPE_FIND && type != TYPE_CACHE
								&& type != TYPE_FIND_SQL && type != TYPE_CACHE_SQL)
							throw new IllegalArgumentException(
									"@PageSize shouldn't be used on "
											+ method.getDeclaringClass()
													.getName() + "."
											+ method.getName());
						pageSizeIdx = i;
						break;
					}
			}
		}

		// 处理属性引用参数
		while (true) {
			Matcher m = P_PROP_PARAM.matcher(hql);
			if (!m.find())
				break;

			String name = m.group(1);

			// 将.替换为三个下划线
			String replace = m.group(0);
			replace = replace.replaceAll("\\.", "___");
			hql = m.replaceFirst(replace);

			if (params.contains(name)) {
				params.add(replace.substring(1));
			} else if (likes.containsKey(name)) {
				likes.put(replace.substring(1), likes.get(name));
			} else if (replaces.containsKey(name)) {
				replaces.put(replace.substring(1), replaces.get(name));
			} else
				throw new IllegalArgumentException(
						"Hql reference an undefined parameter '" + name
								+ "' on "
								+ method.getDeclaringClass().getName() + "."
								+ method.getName());

			props.put(replace.substring(1),
					new ParamProperty(replace, name, m.group(2)));
		}

		int idx = 0;
		Matcher m = P_PART.matcher(hql);
		while (m.find()) {
			if (m.start() != idx)
				parts.add(new QueryPart(method, false, hql.substring(idx,
						m.start()), params, likes, replaces, this.funcs));
			parts.add(new QueryPart(method, true, m.group(1), params, likes,
					replaces, this.funcs));
			idx = m.end();
		}
		if (idx != hql.length())
			parts.add(new QueryPart(method, false, hql.substring(idx), params,
					likes, replaces, this.funcs));

		Class<?> ret = method.getReturnType();
		switch (type) {
		case TYPE_FIND:
			if (ret == List.class)
				retType = RETURN_LIST;
			else if (ret == PageList.class)
				retType = RETURN_PAGE;
			else if (ret.getName().equals("void"))
				retType = RETURN_VOID;
			else
				retType = RETURN_OBJECT;
			break;
		case TYPE_FIND_SQL:
			if (ret == List.class)
				retType = RETURN_LIST;
			else if (ret == PageList.class)
				retType = RETURN_PAGE;
			else if (ret.getName().equals("void"))
				retType = RETURN_VOID;
			else
				retType = RETURN_OBJECT;
			break;
		case TYPE_CACHE:
		case TYPE_CACHE_SQL:
			if (ret == List.class)
				retType = RETURN_LIST;
			else if (ret == PageList.class)
				retType = RETURN_PAGE;
			else
				throw new IllegalArgumentException(
						"@Cache method must return List or PageList type: "
								+ method.getDeclaringClass().getName() + "."
								+ method.getName());

			break;
		case TYPE_UPDATE:
		case TYPE_DELETE:
		case TYPE_UPDATE_SQL:
		case TYPE_DELETE_SQL:
			if (ret.getName().equals("int") && !(ret == Integer.class))
				retType = RETURN_OBJECT;
			else if (ret.getName().equals("void"))
				retType = RETURN_VOID;
			else
				throw new IllegalArgumentException(
						"@Update or @Delete method must return int: "
								+ method.getDeclaringClass().getName() + "."
								+ method.getName());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object invoke(Object proxy, Method method, Object[] args)
			throws NoSuchMethodException, InvocationTargetException,
			IllegalAccessException {
		Object fromCache = null;
		CacheMetaData<T> cacheMetaData = null;
		Object key = null;
		org.springframework.cache.Cache theCache = null;

		if (type == TYPE_CACHE || type == TYPE_CACHE_SQL) {
//			if(args==null)
//				key = keyGenerator.generate(proxy, method, new Object[]{});
//			else 
				key = keyGenerator.generate(proxy, method, args);
			if (cacheManager != null) {
				theCache = cacheManager.getCache(cache.name());
				fromCache = theCache.get(key);
			}

			if (fromCache != null) {
				fromCache = ((ValueWrapper) fromCache).get();

				if (fromCache instanceof CacheList)
					cacheMetaData = ((CacheList) fromCache).getMetaData();
				else if (fromCache instanceof CachePageList)
					cacheMetaData = ((CachePageList) fromCache).getMetaData();
				else {
					fromCache = null;
				}
			}

			if (cacheMetaData != null
					&& System.currentTimeMillis() - cacheMetaData.getSyncTime() < cache
							.ttl()) // 缓存有效
				return fromCache;
		}

		HashMap<String, Object> values = new HashMap<String, Object>();

		for (String p : params.toArray(new String[params.size()])) {
			if (positions.containsKey(p))
				values.put(p, args[positions.get(p)]);
			else {
				ParamProperty pp = props.get(p);
				values.put(p, pp.getValue(args[positions.get(pp.getParam())]));
			}
		}
		for (String p : likes.keySet()) {
			Object val = null;
			if (positions.containsKey(p))
				val = args[positions.get(p)];
			else {
				ParamProperty pp = props.get(p);
				val = pp.getValue(args[positions.get(pp.getParam())]);
			}
			if (val != null) {
				Like l = likes.get(p);
				String tmpval = val.toString().replaceAll("_", "~_").replaceAll("%", "~%");
				if (l.prefix())
					tmpval = "%" + tmpval;
				if (l.suffix())
					tmpval = tmpval + "%";
				val = tmpval;
			}
			values.put(p, val);
		}
		for (String p : replaces.keySet()) {
			if (positions.containsKey(p))
				values.put(p, args[positions.get(p)]);
			else {
				ParamProperty pp = props.get(p);
				values.put(p, pp.getValue(args[positions.get(pp.getParam())]));
			}
		}
		for (String f : funcs.keySet())
			values.put(f, funcs.get(f).exec(method, args, pans));

		HashSet<String> paramsInUse = new HashSet<String>();

		StringBuffer sb = new StringBuffer();
		for (QueryPart part : parts) {
			String p = part.get(values);
			if (p != null) {
				sb.append(p);
				for (String pm : part.getParamDepends()){
					paramsInUse.add(pm);
				}
			}
		}

		String query = sb.toString();
		Query queryObject;

		switch (type) {
		case TYPE_CACHE:
		case TYPE_CACHE_SQL:
		case TYPE_FIND:
		case TYPE_FIND_SQL:
			PageList<T> pageList = null;
			boolean paging = false;
			int pageNum = -1;
			int pageSize = -1;

			if (pageNumIdx >= 0 && pageSizeIdx >= 0) {
				paging = true;
				pageNum = (Integer) args[pageNumIdx];
				pageSize = (Integer) args[pageSizeIdx];

				if (pageNum <= 0)
					pageNum = 1;
				if (pageSize <= 0)
					pageSize = 1;
			}

			CachePageList<T> cachePageList = null;

			if (retType == RETURN_PAGE || type == TYPE_CACHE
					|| type == TYPE_CACHE_SQL) {
				if (type == TYPE_FIND || type == TYPE_FIND_SQL)
					pageList = new PageList<T>();
				else
					pageList = cachePageList = new CachePageList<T>(
							method.getName(), args);

				CountHelper ch = new CountHelper(query);
				if (ch.getFrom() == null)
					if (type == TYPE_CACHE || type == TYPE_FIND)
						throw new IllegalArgumentException(
								"Unable to conver hql to count hql '" + query);
					else
						throw new IllegalArgumentException(
								"Unable to conver sql to count sql '" + query);

				String q = null;
				boolean grouping = ch.getFrom().toLowerCase().indexOf(GROUP_BY) > 0;

				if (type == TYPE_FIND || type == TYPE_FIND_SQL) {
					if (grouping)
						q = "select count(*) from (select " + ch.getSelect()
								+ " from " + ch.getFrom() + ") ";
					else
						q = "select count(*) from " + ch.getFrom();
				} else {
					if (type == TYPE_CACHE)
						q = "select count(*), max(" + cache.alias() + "."
								+ updateTimeProp + ") from " + ch.getFrom();
					else
						q = "select count(*), max(" + cache.alias() + "."
								+ updateTimeCol.name() + ") from "
								+ ch.getFrom();
				}

				if (type == TYPE_FIND_SQL || type == TYPE_CACHE_SQL)
					queryObject = factory.getCurrentSession().createSQLQuery(q);
				else
					queryObject = factory.getCurrentSession().createQuery(q);

				for (String param : paramsInUse)
					HibernateDaoBaseMethod.applyNamedParameterToQuery(
							queryObject, param, values.get(param));

				Object c = queryObject.list().get(0);
				if (type == TYPE_FIND || type == TYPE_FIND_SQL) {
					if (c instanceof Long)
						pageList.setTotalCount(((Long) c).intValue());
					else if (c instanceof BigDecimal)
						pageList.setTotalCount(((BigDecimal) c).intValue());
					else if (c instanceof BigInteger)
						pageList.setTotalCount(((BigInteger) c).intValue());
					else
						pageList.setTotalCount((Integer) c);
				} else {
					Object[] cs = (Object[]) c;
					if (cs[0] instanceof Long)
						pageList.setTotalCount(((Long) cs[0]).intValue());
					else if (cs[0] instanceof BigDecimal)
						pageList.setTotalCount(((BigDecimal) cs[0]).intValue());
					else
						pageList.setTotalCount((Integer) cs[0]);

					cachePageList.setTimestamp(cs[1] == null ? 0
							: ((Date) cs[1]).getTime());

					if (cachePageList.getMetaData().equals(cacheMetaData)) { // 缓存有效
						cacheMetaData.updateSyncTime();
						theCache.put(key, fromCache);
						return fromCache;
					}
				}

				if (paging) {
					pageList.setPageSize(pageSize);
					pageList.setTotalPage((int) Math.ceil((double) pageList
							.getTotalCount() / pageList.getPageSize()));
					if (pageNum > pageList.getTotalPage())
						pageNum = pageList.getTotalPage();
					pageList.setCurrentPage(pageNum);
				}
			}

			List<?> result = null;

			if (pageList == null || pageList.getTotalCount() != 0) {
				if (type == TYPE_FIND_SQL || type == TYPE_CACHE_SQL) {
					SQLQuery q = factory.getCurrentSession().createSQLQuery(
							query);
					queryObject = q;
					if (entityClass != null&&useEntity)
						q.addEntity(entityClass);
				} else
					queryObject = factory.getCurrentSession()
							.createQuery(query);

				for (String param : paramsInUse)
					HibernateDaoBaseMethod.applyNamedParameterToQuery(
							queryObject, param, values.get(param));
				if (paging) {
					queryObject.setFirstResult((pageNum - 1) * pageSize);
					queryObject.setMaxResults(pageSize);
				}
				result = queryObject.list();
			}

			switch (retType) {
			case RETURN_LIST:
				if (type == TYPE_FIND || type == TYPE_FIND_SQL)
					return result;
				
				if(result==null){
					result = new ArrayList<T>();
				}
				CacheList<T> list = new CacheList<T>(method.getName(), args);
				list.addAll((ArrayList<T>) result);
				list.setTimestamp(cachePageList.getMetaData().getTimestamp());
				list.setCount(cachePageList.getMetaData().getCount());
				theCache.put(key, list);
				return list;
			case RETURN_PAGE:
				if (result == null)
					result = new ArrayList<T>();
				pageList.setList(result);

				if (type == TYPE_CACHE || type == TYPE_CACHE_SQL)
					theCache.put(key, pageList);

				return pageList;
			case RETURN_VOID:
				return null;
			case RETURN_OBJECT:
				return result.size() > 0 ? result.get(0) : null;
			}
		case TYPE_UPDATE:
		case TYPE_DELETE: {
			queryObject = factory.getCurrentSession().createQuery(query);
			for (String param : paramsInUse)
				HibernateDaoBaseMethod.applyNamedParameterToQuery(queryObject,
						param, values.get(param));

			int ret = queryObject.executeUpdate();

			if (retType == RETURN_VOID)
				return null;

			return ret;
		}
		case TYPE_UPDATE_SQL:
		case TYPE_DELETE_SQL: {
			queryObject = factory.getCurrentSession().createSQLQuery(query);
			for (String param : paramsInUse)
				HibernateDaoBaseMethod.applyNamedParameterToQuery(queryObject,
						param, values.get(param));

			int ret = queryObject.executeUpdate();

			if (retType == RETURN_VOID)
				return null;

			return ret;
		}
		}

		return null;
	}
}
