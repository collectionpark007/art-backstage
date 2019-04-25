/*
 * 
 * 
 * 
 */
package com.art.config.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 登录令牌
 * 
 * 
 * 
 */
public class AuthenticationToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 5898441540965086534L;


	/**
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 */
	public AuthenticationToken(String username, String password, boolean rememberMe) {
		super(username, password, rememberMe);
	}


}