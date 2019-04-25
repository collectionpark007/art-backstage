package com.art.support.annotation;

import java.lang.annotation.*;


/**
 * Mybatis的表名的注解
 * <p>
 *
 * @Author: never
 * @Date: 2019/4/16 下午10:32
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTable {
    String value() default "";
}
