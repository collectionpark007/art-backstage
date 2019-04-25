/*
 *
 *
 *
 */
package com.art.service;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;

/**
 * Service - RSA安全
 *
 *
 *
 */
public interface RSAService {

	/**
	 * 生成密钥(添加私钥至Session并返回公钥)
	 *
	 * @param request
	 *            httpServletRequest
	 * @return 公钥
	 */
	RSAPublicKey generateKey(HttpServletRequest request);

	/**
	 * 移除私钥
	 *
	 * @param request
	 *            httpServletRequest
	 */
	void removePrivateKey(HttpServletRequest request);

	/**
	 * 解密参数
	 *
	 * @param name
	 *            参数名称
	 * @param request
	 *            httpServletRequest
	 * @return 解密内容
	 */
	String decryptParameter(String name, HttpServletRequest request);


	/**
	 * 生成密钥（手机版使用该生成方法）
	 * @param request
	 * @return
	 */
	String generateRSAKey(HttpServletRequest request);

	/**
	 * 解密参数(手机版接口使用）
	 *
	 * @param name
	 *            参数名称
	 * @param request
	 *            httpServletRequest
	 * @return 解密内容
	 */
	String decryptRSAParameter(String name, HttpServletRequest request);

}