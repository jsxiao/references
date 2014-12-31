package com.aggrepoint.winlet;

import java.util.Hashtable;
import java.util.Vector;

import com.aggrepoint.winlet.spring.def.ViewDef;
import com.aggrepoint.winlet.spring.def.WinletDef;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 */
public class ViewInstance {
	WinletInstance wis;
	long viewId;
	Object winlet;
	ViewDef viewDef;
	ViewInstance parent;
	Vector<ViewInstance> vecSubViews;
	Hashtable<String, String> htParams;

	public ViewInstance(WinletInstance wi, long viewid, Object winlet,
			ViewDef viewDef, Hashtable<String, String> params) {
		wis = wi;
		viewId = viewid;
		this.winlet = winlet;
		this.viewDef = viewDef;
		htParams = params;
		parent = null;
		vecSubViews = null;
	}

	public void setParams(Hashtable<String, String> params) {
		htParams = params;
	}

	public Hashtable<String, String> getParams() {
		return htParams;
	}

	public String getParam(String name) {
		String str = null;
		if (htParams != null)
			str = htParams.get(name);
		if (str != null)
			return str;
		if (parent == null)
			return null;
		return parent.getParam(name);
	}

	public long getId() {
		return viewId;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public Object getWinlet() {
		return winlet;
	}

	public void clearSub() {
		vecSubViews = null;
	}

	static long getViewId(long parent, int sub) {
		if (parent < 100l) {
			return sub * 100 + parent;
		} else if (parent < 10000l) {
			return sub * 10000l + parent;
		} else if (parent < 1000000l) {
			return sub * 1000000l + parent;
		} else if (parent < 100000000l) {
			return sub * 100000000l + parent;
		} else if (parent < 10000000000l) {
			return sub * 10000000000l + parent;
		}
		return sub * 1000000000000l + parent;
	}

	/**
	 * @param winlet
	 *            可选，如果不指定则以当前ViewInstance对应的winlet作为要添加的winlet
	 * @param viewname
	 *            要添加的winlet的视图
	 * @param htParams
	 * @return
	 * @throws Exception
	 */
	public ViewInstance addSub(Object winlet, String viewname,
			Hashtable<String, String> htParams) throws Exception {
		WinletDef def;

		if (winlet == null) {
			def = viewDef.getWinletDef();
			winlet = this.winlet;
		} else {
			def = WinletDef.getDef(winlet.getClass());
		}

		for (ViewDef view : def.getViews())
			if (view.getName().equals(viewname)) {
				if (vecSubViews == null)
					vecSubViews = new Vector<ViewInstance>();
				ViewInstance inst = new ViewInstance(wis, getViewId(viewId,
						vecSubViews.size() + 1), winlet, view, htParams);
				inst.parent = this;
				vecSubViews.add(inst);
				return inst;
			}

		throw new Exception("Unable to find view \"" + viewname
				+ "\" in winlet " + winlet.getClass().getName());
	}

	ViewInstance find(long viewid) {
		if (viewId == viewid)
			return this;
		if (vecSubViews != null
				&& Long.toString(viewid).endsWith(Long.toString(viewId))) {
			for (ViewInstance view : vecSubViews) {
				if (viewid == view.viewId)
					return view;

				if (Long.toString(viewid).endsWith(Long.toString(view.viewId)))
					return view.find(viewid);
			}
		}
		return null;
	}

	private void getUpdateViews(Vector<ViewToUpdate> updates,
			Vector<String> vecUpdates, String defaultWinletName) {
		for (ViewToUpdate t : updates) {
			String winletName = t.winletName == null ? defaultWinletName
					: t.winletName;

			if (viewDef.getWinletDef().getName().equals(winletName)
					&& t.viewName.equals(viewDef.getName())) {
				if (t.ensureVisible)
					vecUpdates.add("!" + viewId);
				else
					vecUpdates.add(Long.toString(viewId));
				return;
			}
		}

		if (vecSubViews != null)
			for (ViewInstance vi : vecSubViews)
				vi.getUpdateViews(updates, vecUpdates, defaultWinletName);
	}

	public String translateUpdateViews(ReqInfo req, String from) {
		if (from == null || from.equals("")) {
			return "";
		}

		boolean bUpdateWholeWin = false;
		Vector<ViewToUpdate> updates = ViewToUpdate.parse(from);
		Vector<String> vecUpdates = new Vector<String>();
		Vector<ViewToUpdate> toRemove = new Vector<ViewToUpdate>();

		for (ViewToUpdate update : updates) {
			if (update.winletName == null) {
				if (update.viewName.equals("window")) {
					bUpdateWholeWin = true;
					toRemove.add(update);
				} else if (update.viewName.equals("parent")) {
					if (parent != null)
						update.viewName = Long.toString(parent.viewId);
					else {
						bUpdateWholeWin = true;
						toRemove.add(update);
					}
				} else if (update.viewName.equals("area"))
					return "area";
				else if (update.viewName.equals("page"))
					return "page";
			}
		}

		updates.removeAll(toRemove);

		// { Match all other windows within same page
		for (WinletInstance wi : WinletManager.getWinInstancesInPage(req)) {
			if (wi == wis)
				continue;

			wi.viewInstance.getUpdateViews(updates, vecUpdates, viewDef
					.getWinletDef().getName());
		}
		// }

		if (bUpdateWholeWin)
			vecUpdates.add(Long.toString(wis.iid));
		else
			wis.viewInstance.getUpdateViews(updates, vecUpdates, viewDef
					.getWinletDef().getName());

		StringBuffer sb = new StringBuffer();
		boolean bFirst = true;
		for (String str : vecUpdates) {
			if (bFirst)
				bFirst = false;
			else
				sb.append(",");
			sb.append(str);
		}

		return sb.toString();
	}

	public Object getEmbedded(Class<?> c) throws Exception {
		if (vecSubViews == null)
			return null;

		for (ViewInstance sub : vecSubViews) {
			if (sub.winlet.getClass().equals(c))
				return sub.winlet;
		}

		Object winlet = null;
		for (ViewInstance sub : vecSubViews) {
			winlet = sub.getEmbedded(c);
			if (winlet != null)
				return winlet;
		}
		return null;
	}

	public Object getEmbedded(String name) throws Exception {
		if (vecSubViews == null)
			return null;

		for (ViewInstance sub : vecSubViews) {
			if (sub.getViewDef().getWinletDef().getName().equals(name))
				return sub.winlet;
		}

		Object winlet = null;
		for (ViewInstance sub : vecSubViews) {
			winlet = sub.getEmbedded(name);
			if (winlet != null)
				return winlet;
		}
		return null;
	}
}
