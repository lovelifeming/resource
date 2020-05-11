package com.zsm.sb.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


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
 * 404	SERVICE_NOT_EXIST	服务未发现
 * 405	HTTP_ACTION_UNSUPPORTED	服务不支持该HTTP协议动词
 * 406	VERSION_MISSING	服务版本错误
 * 407	METHOD_MISSING	服务方法错误
 * 408	ACCESSTOKEN_MISSING	参数AccessToken无效或者已经过期
 * 409	APPKEY_MISSING	参数AppKey错误
 * 410	SECRETKEY_MISSING	参数SecretKey错误
 * 500	SERVICE_UNAVAILABLE	服务不可用
 * 503	REJECTED_SERVICE	系统过载,拒绝服务
 * 504	SERVICE_TIMEOUT	访问超时
 * 505	INVOKETIMES_EXCEED	服务访问次数受限
 * 506	INVALID_PERMISSION	服务访问权限未开放
 * 508	SERVICE_UNAVAILABLE	接入服务不可用
 * <p>
 * 控制层消息返回类
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JacksonXmlRootElement(localName = "root")
@Api(description = "消息返回对象")
@ApiModel("返回结果集")
public class ReturnMsg<T>
{
    /**
     * 请求成功返回
     */
    public static <T> ReturnMsg<T> generatorSuccessMsg(T data)
    {
        return generatorMsg(data, "", true, 202);
    }

    /**
     * 请求成功返回
     */
    public static <T> ReturnMsg<T> generatorSuccessMsg(T data, String message)
    {
        return generatorMsg(data, message, true, 202);
    }

    /**
     * 请求失败返回
     */
    public static <T> ReturnMsg<T> generatorFailMsg(String message, Integer code)
    {
        return generatorMsg((T)"", message, false, code);
    }

    public static <T> ReturnMsg<T> generatorMsg(T data, String message, Boolean success, Integer code)
    {
        ReturnMsg<T> msg = new ReturnMsg<>();
        msg.setData(data);
        msg.setMessage(message);
        msg.setSuccess(success);
        msg.setStatusCode(code.toString());
        return msg;
    }

    @XmlElement(name = "success")
    @ApiModelProperty("是否成功")
    private boolean success;

    @XmlElement(name = "statusCode")
    @ApiModelProperty("状态码")
    private String statusCode;

    @XmlElement(name = "message")
    @ApiModelProperty("消息提示")
    private String message;

    @JacksonXmlElementWrapper(localName = "datas")
    @JacksonXmlProperty(localName = "data")
    @ApiModelProperty("结果数据")
    private T data;

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(String statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ReturnMsg{" +
               "statusCode='" + statusCode + '\'' +
               ", message='" + message + '\'' +
               ", data=" + data +
               '}';
    }
}
