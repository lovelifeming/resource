package com.zsm.encrypt.common;

/**
 * @Author: zengsm.
 * @Date:Created in 2021-04-06 22:01.
 * @Description: 解密注解
 * <p>
 * 加了此注解的接口(true)将进行数据解密操作(post的body) 可
 * 以放在类上，可以放在方法上
 */


import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 解密注解
 *
 * 加了此注解的接口(true)将进行数据解密操作(post的body) 可
 *    以放在类上，可以放在方法上
 */
public @interface DecryptRequest
{
    /**
     * 是否对body进行解密
     */
    boolean value() default true;
}
