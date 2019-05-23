package com.zsm.apidoc.apijson.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2019-05-23 23:35.
 * @Modified By:
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiSingleParam
{
    String name() default "";

    String value() default "";

    Class<?> type() default String.class;

    String example() default "";

    boolean allowMultiple() default false;
}
