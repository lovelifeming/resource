package com.zsm.apidoc.apijson.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2019-05-23 23:34.
 * @Modified By:
 */
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiJsonObject
{
    /**
     * 对象属性值
     * @return
     */
    ApiJsonProperty[] value();

    /**
     * 返回结果
     * @return
     */
    ApiJsonResult result() default @ApiJsonResult({});

    /**
     * 对象名称
     * @return
     */
    String name() default "";
}
