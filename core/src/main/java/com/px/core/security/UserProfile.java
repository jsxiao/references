package com.px.core.security;

public interface UserProfile {
	
	/**
	 * 主键
	 * @return
	 */
	public Integer getId();

	/**
	 * 登录用户名
	 * @return
	 */
	public String getUserName();

	/**
	 * 登录密码
	 * @return
	 */
	public String getPassword();
	
	/**
	 * 真实姓名
	 * @return
	 */
	public String getRealName();
	
	/**
	 * 身份证号
	 * @return
	 */
	public String getIdCard();
	
	/**
	 * 是否匿名用户
	 * @return
	 */
	public boolean isAnonymous();
	
	/**
	 * 是否实名
	 * @return
	 */
	public boolean isReal();

	/**
	 * 手机号码
	 * @return
	 */
	public String getPhone();

	/**
	 * 存管手机号
	 * @return
	 */
	public String getApiPhone();
}
