package com.px.core.http;

public interface ResponseBody {

	/**
	 * 返回内容
	 * @return
	 */
	public String getBody();
	
	/**
	 * 响应状态
	 * @return
	 */
	public int getStatus();
	
	/**
	 * 将返回的内容转换为对象(返回内容必须为JSON格式)
	 * @return
	 */
	public <T> T toObject(Class<T> clazz);
	
	public <T> T getObject();
}
