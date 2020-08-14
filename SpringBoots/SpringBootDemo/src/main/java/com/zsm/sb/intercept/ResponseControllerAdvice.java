package com.zsm.sb.intercept;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsm.sb.model.ResultVO;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * @Author: zeng.
 * @Date:Created in 2020-08-14 15:04.
 * @Description:全局处理响应数据--响应增强类 接口返回统一响应体 + 异常也返回统一响应体，
 */
// 可以改为 @ControllerAdvice 注解来拦截所有Controller的处理结果
@RestControllerAdvice(basePackages = {"com.zsm.sb.controller"})     // 添加需要扫描的包
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object>
{
    // 1.关于哪些请求要执行beforeBodyWrite，返回true执行，返回false不执行
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass)
    {
        // 如果接口返回的类型本身就是ResultVO那就没有必要进行额外的操作，返回false
        System.out.println("============" + !methodParameter.getParameterType().equals(ResultVO.class));
        return !methodParameter.getParameterType().equals(ResultVO.class);
    }

    // 2.如果接口返回的类型本身不是ResultVO，那就将原本的数据包装在ResultVO里再返回
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse)
    {
        // String类型不能直接包装，所以要进行些特别的处理
        if (methodParameter.getParameterType().equals(String.class))
        {
            System.out.println(o instanceof String);
            ObjectMapper objectMapper = new ObjectMapper();
            try
            {
                // 将String转换再将数据包装在ResultVO里，再转换为json字符串响应给前端
                return objectMapper.writeValueAsString(ResultVO.succes(o));
            }
            catch (JsonProcessingException e)
            {
                throw new RuntimeException("返回String类型错误：" + o);
            }
        }
        // 将原本的数据包装在ResultVO里
        return ResultVO.succes(o);
    }
}
