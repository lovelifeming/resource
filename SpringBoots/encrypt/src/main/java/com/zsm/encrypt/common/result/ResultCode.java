package com.zsm.encrypt.common.result;

import com.zsm.encrypt.common.result.IErrorCode;


/**
 * @Author: zengsm.
 * @Date:Created in 2021-04-06 22:48.
 * @Description:
 */
public enum ResultCode implements IErrorCode
{
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");

    private long code;

    private String message;

    private ResultCode(long code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public long getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}
