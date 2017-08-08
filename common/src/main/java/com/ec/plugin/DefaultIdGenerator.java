package com.ec.plugin;

import com.ec.utils.StringUtils;

public class DefaultIdGenerator implements IdGenerator{

	@Override
	public String getUUID() {
		return StringUtils.UUID();
	}

}
