package com.zsm.encrypt.common;

/**
 * @Author: zengsm.
 * @Date:Created in 2021-04-06 22:06.
 * @Description: 请求数据接收处理类<br>
 * <p>
 * 对加了@Decrypt的方法的数据进行解密操作<br>
 * 只对 @RequestBody 参数有效
 * <p>
 * 请求数据接收处理类<br>
 * <p>
 * 对加了@Decrypt的方法的数据进行解密操作<br>
 * 只对 @RequestBody 参数有效
 */
/**
 * 请求数据接收处理类<br>
 *
 * 对加了@Decrypt的方法的数据进行解密操作<br>
 * 只对 @RequestBody 参数有效
 */


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;


@ControllerAdvice
@ConditionalOnProperty(prefix = "spring.crypto.request.decrypt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DecryptRequestBodyAdvice implements RequestBodyAdvice
{
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType)
    {
        return true;
        //return returnType.hasMethodAnnotation(ResponseBody.class);
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
    {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType)
        throws IOException
    {
        if (NeedCrypto.needDecrypt(parameter))
        {
            return new DecryptHttpInputMessage(inputMessage, "UTF-8");
        }
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType)
    {
        return body;
    }
}
