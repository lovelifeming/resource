package com.zsm.apidoc.apijson.model;

import com.zsm.apidoc.apijson.util.CommonConst;

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
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiJsonResult
{
    /**
     * 返回值集合
     *
     * @return
     */
    String[] value();

    /**
     * 返回值名称
     *
     * @return
     */
    String name() default "";

    /**
     * 返回值类型
     *
     * @return
     */
    String type() default CommonConst.RESULT_TYPE_NORMAL_FINAL;
}
