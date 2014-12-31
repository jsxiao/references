package com.aggrepoint.winlet;

import com.aggrepoint.winlet.spring.def.ViewDef;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 */
public class WinletInstance {
	long iid;
	ViewInstance viewInstance;

	public WinletInstance(long iid, ViewDef def, Object winlet) {
		this.iid = iid;
		viewInstance = new ViewInstance(this, iid, winlet, def, null);
	}

	public ViewInstance findView(long vid) {
		return viewInstance.find(vid);
	}
}
