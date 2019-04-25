package com.art.utils;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

/**
 *
 * 
 *        属性操作方法
 * 
 */
public class PropertiesUtils {

	private Properties prop = new Properties();
	private static PropertiesUtils propertiesUtils;

	public static PropertiesUtils getInstance(){

		if (propertiesUtils == null){
			propertiesUtils = new PropertiesUtils();
		}
		return propertiesUtils;
	}

	private PropertiesUtils() {
		// default configuration file
		this.init("application.yml");
	}

	public PropertiesUtils(String fileName) {
		this.init(fileName);
	}

	private void init(String fileName) {
		//
		YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
		bean.setResources(new ClassPathResource(fileName));
		prop = bean.getObject();
	}

	/**
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	public String getValue(String key) {
		String val = prop.getProperty(key);
		if (val != null) {
			return val.trim();
		}
		return "";
	}


}
