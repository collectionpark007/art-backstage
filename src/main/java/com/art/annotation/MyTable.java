package com.art.annotation;

import java.lang.annotation.*;


/**
 * Mybatis的表名的注解
 * <p>
 *
 * @Author: never
 * @Date: 2019/04/22 10:32
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTable {
    String value() default "";
}
