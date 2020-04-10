package com.zsm.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 返回结果集
 */
@Data
@ApiModel("返回结果集")
public class ResultSet<T>
{
    @ApiModelProperty("是否成功")
    private boolean success;

    @ApiModelProperty("状态码")
    private String statusCode;

    @ApiModelProperty("消息提示")
    private String msg;

    @ApiModelProperty("结果数据")
    private T data;

    /**
     * 请求成功返回
     */
    public static <T> ResultSet<T> success(T t)
    {
        ResultSet<T> rs = new ResultSet<>();
        rs.setSuccess(true);
        rs.setStatusCode("200");
        rs.setMsg("success");
        rs.setData(t);
        return rs;
    }

    /**
     * 请求失败返回
     */
    public static <T> ResultSet<T> fail(String code, String msg)
    {
        ResultSet<T> rs = new ResultSet<>();
        rs.setSuccess(false);
        rs.setStatusCode(code);
        rs.setMsg(msg);
        return rs;
    }
}
