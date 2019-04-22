package com.art.annotation;

import java.lang.annotation.*;

/**
 * mybatis的ID主键的注解
 * <p>
 *
 * @Author: never
 * @Date: 2019/04/22 10:32
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyId {
    long value() default 1L;
}
