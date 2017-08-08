package com.px.core.tx;

import java.io.Serializable;

import javax.persistence.Transient;

public class BasEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	private int pageNo = 1;
	@Transient
	private int pageSize = 20;

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
