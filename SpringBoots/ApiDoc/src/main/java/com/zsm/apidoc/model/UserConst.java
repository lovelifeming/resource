package com.zsm.apidoc.model;

import com.alibaba.fastjson.JSONObject;
import com.zsm.apidoc.apijson.model.ApiSingleParam;


public class UserConst
{
    @ApiSingleParam(value = "返回消息", example = "test")
    public static final String JSON_RESULT_MESSAGE = "message";

    @ApiSingleParam(value = "返回码", example = "0", type = Integer.class)
    public static final String JSON_RESULT_CODE = "code";

    @ApiSingleParam(value = "返回数据", example = "data", type = JSONObject.class)
    public static final String JSON_RESULT_DATA = "data";

    @ApiSingleParam(value = "请求参数用户名", example = "test")
    public static final String JSON_USER_NAME = "userName";

    @ApiSingleParam(value = "请求参数用户年龄", example = "18", type = Integer.class)
    public static final String JSON_USER_AGE = "age";

    @ApiSingleParam(value = "请求参数用户性别", example = "male", type = Integer.class)
    public static final String JSON_USER_SEX = "sex";

}