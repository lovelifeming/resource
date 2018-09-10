package com.zsm.sb.model;

import java.util.HashMap;
import java.util.Map;


/**
 * 200 OK - [GET]：服务器成功返回用户请求的数据，该操作是幂等的（Idempotent）。
 * 201 CREATED - [POST/PUT/PATCH]：用户新建或修改数据成功。
 * 202 Accepted - [*]：表示一个请求已经进入后台排队（异步任务）
 * 204 NO CONTENT - [DELETE]：用户删除数据成功。
 * 400 INVALID REQUEST - [POST/PUT/PATCH]：用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。
 * 401 Unauthorized - [*]：表示用户没有权限（令牌、用户名、密码错误）。
 * 403 Forbidden - [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的。
 * 404 NOT FOUND - [*]：用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
 * 406 Not Acceptable - [GET]：用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。
 * 410 Gone -[GET]：用户请求的资源被永久删除，且不会再得到的。
 * 422 Unprocesable entity - [POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误。
 * 500 INTERNAL SERVER ERROR - [*]：服务器发生错误，用户将无法判断发出的请求是否成功。
 * 502 网关错误
 * 503 Service Unavailable
 * 504 网关超时
 * <p>
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
