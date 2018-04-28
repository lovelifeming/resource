package com.zsm.sb.model;

import java.util.HashMap;
import java.util.Map;


/**
 * 控制层消息返回类
 */
public class ReturnMsg
{
    // 状态码 true成功 false失败
    private boolean success;

    //状态提示信息,用于错误信息提示
    private String message;

    //返回的数据，可以是JSON，Map
    private Map<String, Object> data = new HashMap<>();

    public static ReturnMsg success()
    {
        return getReturnMsg(true, "request success!", new HashMap<>());
    }

    public static ReturnMsg success(String message)
    {
        return getReturnMsg(true, message, new HashMap<>());
    }

    public static ReturnMsg success(Map<String, Object> data)
    {
        return getReturnMsg(true, "request success!", data);
    }

    public static ReturnMsg success(String message, Map<String, Object> data)
    {
        return getReturnMsg(true, message, data);
    }

    public static ReturnMsg fail()
    {
        return getReturnMsg(false, "request fail!", new HashMap<>());
    }

    public static ReturnMsg fail(String message)
    {
        return getReturnMsg(false, message, new HashMap<>());
    }

    public static ReturnMsg fail(Map<String, Object> data)
    {
        return getReturnMsg(false, "request fail!", data);
    }

    public static ReturnMsg fail(String message, Map<String, Object> data)
    {
        return getReturnMsg(false, message, data);
    }

    public static ReturnMsg getReturnMsg(boolean success, String message, Map<String, Object> data)
    {
        ReturnMsg result = new ReturnMsg();
        result.setSuccess(success);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public ReturnMsg add(String key, Object value)
    {
        this.getData().put(key, value);
        return this;
    }

    public ReturnMsg modifyMsg(Map<String, Object> data)
    {
        this.setData(data);
        return this;
    }

    public ReturnMsg modifyMsg(String message)
    {
        this.setMessage(message);
        return this;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Map<String, Object> getData()
    {
        return data;
    }

    public void setData(Map<String, Object> data)
    {
        this.data = data;
    }
}
