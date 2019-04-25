package com.art.support.annotation;

import java.lang.annotation.*;

/**
 * Mybatis的列的注解
 * <p>
 *
 * @Author: never
 * @Date: 2019/4/16 下午10:32
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyColumn {
    String value() default "";
}
