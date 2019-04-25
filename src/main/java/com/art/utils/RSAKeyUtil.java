package com.art.utils;

import org.bouncycastle.openssl.PEMWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.PublicKey;


public final class RSAKeyUtil {

	public static final String SESSION_SECURITY_PRIVATE_KEY = "SESSION_SECURITY_PRIVATE_KEY";
	public static final String SESSION_SECURITY_PUBLIC_KEY = "SESSION_SECURITY_PUBLIC_KEY";

	/**
	 * 返回PEM格式的public公钥.<br>
	 * 如果发生错误，返回空字符串
	 * 
	 * @param pubkey
	 * @return
	 */
	public static String getPEMkey(PublicKey pubkey) {
		StringWriter writer = new StringWriter();
		PEMWriter pem = new PEMWriter(writer);
		try {
			pem.writeObject(pubkey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			pem.flush();
			writer.flush();
			writer.close();
			pem.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return writer.toString();
	}

	/**
	 * 返回PEM格式的private私钥.<br>
	 * 如果发生错误，返回空字符串
	 * 
	 * @param prikey
	 * @return
	 */
	public static String getPEMkey(PrivateKey prikey) {
		StringWriter writer = new StringWriter();
		PEMWriter pem = new PEMWriter(writer);
		try {
			pem.writeObject(prikey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			pem.flush();
			writer.flush();
			writer.close();
			pem.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return writer.toString();
	}

	
}