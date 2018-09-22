package com.zsm.sb.sensitive;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/9/20.
 * @Modified By:
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerialize.class)
public @interface SensitiveInfo
{
    SensitiveType value()  default SensitiveType.MOBILE_PHONE;
}
