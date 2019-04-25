package com.art.support.annotation;

import java.lang.annotation.*;

/**
 * 登录拦截
 * @author never
 *
 */

@Documented
@Inherited
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiLoginAuth {

	
	boolean validate() default true;
	
}
