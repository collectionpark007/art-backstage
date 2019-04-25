/*
 *
 *
 *
 */
package com.art.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Utils - Spring
 *
 *
 *
 */
@Component
@Lazy(false)
public final class SpringUtils implements ApplicationContextAware, DisposableBean {

	private static Log log = LogFactory.getLog("SpringUtils");

	/** applicationContext */
	private static ApplicationContext applicationContext;

	/**
	 * 不可实例化
	 */
	private SpringUtils() {
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringUtils.applicationContext = applicationContext;
	}

	public void destroy() throws Exception {
		applicationContext = null;
	}

	/**
	 * 获取applicationContext
	 *
	 * @return applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取实例
	 *
	 * @param name
	 *            Bean名称
	 * @return 实例
	 */
	public static Object getBean(String name) {
		Assert.hasText(name, "Name cannot be enpty");
		return applicationContext.getBean(name);
	}

	/**
	 * 获取实例
	 *
	 * @param name
	 *            Bean名称
	 * @param type
	 *            Bean类型
	 * @return 实例
	 */
	public static <T> T getBean(String name, Class<T> type) {
		Assert.hasText(name, "Name cannot be empty");
		Assert.notNull(type,"Type cannot be null");
		return applicationContext.getBean(name, type);
	}

	/**
	 * 获取国际化消息
	 *
	 * @param code
	 *            代码
	 * @param args
	 *            参数
	 * @return 国际化消息
	 */
	public static String getMessage(String code, Object... args) {
		if (StringUtils.isEmpty(code)){
			return "";
		}
		LocaleResolver localeResolver = getBean("localeResolver", LocaleResolver.class);
		Locale locale = localeResolver.resolveLocale(null);
		MessageSource messageSource = getBean("messageSource", MessageSource.class);
		return messageSource.getMessage(code, args, locale);
	}


	/**
	 * 接口多语言
	 * @param code
	 * @param args
	 * @return
	 */
	public static String getApiMessage(String code, Object... args) {
		if (StringUtils.isEmpty(code)){
			return "";
		}
		LocaleResolver localeResolver = getBean("localeResolver", LocaleResolver.class);
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Locale locale = null;
		if (requestAttributes != null){
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

			locale = localeResolver.resolveLocale(request);

			if (locale == null){
				locale = StringUtils.parseLocale("zh_CN");
			}

		}
		MessageSource messageSource = getBean("messageSource", MessageSource.class);

		return messageSource.getMessage(code, args, locale);
	}
}