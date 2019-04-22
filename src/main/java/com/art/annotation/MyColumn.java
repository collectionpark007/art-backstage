package com.art.annotation;

import java.lang.annotation.*;

/**
 * Mybatis的列的注解
 * <p>
 *
 * @Author: never
 * @Date: 2019/04/22 10:32
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyColumn {
    String value() default "";
}
