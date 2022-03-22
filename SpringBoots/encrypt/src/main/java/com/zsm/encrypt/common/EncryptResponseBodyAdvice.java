package com.zsm.encrypt.common;

import com.zsm.encrypt.common.result.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;
import java.util.Map;

/**
 * @Author: zengsm.
 * @Date:Created in 2021-04-06 22:08.
 * @Description:
 */


/**
 * 请求响应处理类<br>
 * 对加了@Encrypt的方法的数据进行加密操作
 */
@ControllerAdvice
@ConditionalOnProperty(prefix = "spring.crypto.response.encrypt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object>
{
    @Value("${spring.crypto.request.decrypt.charset:UTF-8}")
    private String charset = "UTF-8";

    @Autowired
    @Qualifier("rrCrypto")
    private Crypto crypto;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        boolean encrypt = NeedCrypto.needEncrypt(returnType);

        if( !encrypt ){
            return body;
        }

        if(!(body instanceof CommonResult)){
            return body;
        }

        //只针对ResponseMsg的data进行加密
        CommonResult responseMsg = (CommonResult) body;
        Object data = responseMsg.getData();
        if(null == data){
            return body;
        }
        responseMsg.setData(crypto.encrypt(GsonUtils.toJson(data), charset));
        return responseMsg;
    }
}
