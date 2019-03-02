package com.zsm.validation.model;

import lombok.Data;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2019/2/25 17:37.
 * @Modified By:
 */
@Data
public class RespResult<T>
{
    private RespResult()
    {

    }

    private String message;

    private T data;

    private String Code;

    public static RespResult failure(String message)
    {
        return RespResult.build(message, null, "-1");
    }

    public static <T> RespResult success(String message, T data)
    {
        return RespResult.build(message, data, "0");
    }

    public static <T> RespResult success(String message)
    {
        return RespResult.build(message, null, "0");
    }

    private static <T> RespResult build(String message, T data, String code)
    {
        RespResult respResult = new RespResult();
        respResult.message = message;
        respResult.Code = code;
        respResult.data = data;

        return respResult;
    }
}
