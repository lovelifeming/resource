package com.zsm.encrypt.common;

/**
 * @Author: zengsm.
 * @Date:Created in 2021-04-06 22:03.
 * @Description: 加密注解
 * <p>
 * 加了此注解的接口(true)将进行数据加密操作
 * 可以放在类上，可以放在方法上
 */


import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 加密注解
 *
 *加了此注解的接口(true)将进行数据加密操作
 *    可以放在类上，可以放在方法上
 */
public @interface EncryptResponse
{
    /**
     * 是否对结果加密
     */
    boolean value() default true;
}
