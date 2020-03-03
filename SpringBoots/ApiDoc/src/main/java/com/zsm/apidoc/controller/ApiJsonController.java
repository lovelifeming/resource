package com.zsm.apidoc.controller;

import com.zsm.apidoc.apijson.model.ApiJsonObject;
import com.zsm.apidoc.apijson.model.ApiJsonProperty;
import com.zsm.apidoc.apijson.model.ApiJsonResult;
import com.zsm.apidoc.model.UserConst;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2019-05-24 01:35.
 * @Modified By:
 */
@RestController
@RequestMapping("/apijson/")
@Api(description = "ApiJson请求参数和返回参数接口文档")
public class ApiJsonController
{
    @ApiOperation(value = "测试ApiJson", tags = "tags标签", notes = "获取用户user信息")
    @ApiJsonObject(name = "user-info", value = {
        @ApiJsonProperty(name = UserConst.JSON_USER_NAME),
        @ApiJsonProperty(name = UserConst.JSON_USER_AGE),
        @ApiJsonProperty(name = UserConst.JSON_USER_SEX)},
        result = @ApiJsonResult(name = "返回结果", value = {UserConst.JSON_RESULT_MESSAGE, UserConst.JSON_RESULT_CODE}))
    @ApiResponses({@ApiResponse(code = 200, message = "OK", reference = "user-info"),
        @ApiResponse(code = 401, message = "Unauthorized", reference = "user-info"),
        @ApiResponse(code = 403, message = "Forbidden", reference = "user-info"),
        @ApiResponse(code = 404, message = "NotFound", reference = "user-info")})
    @ApiImplicitParam(name = "params", required = true, dataType = "user-info")
    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    public String testApiJson(@RequestBody String params)
    {
        System.out.println(params);
        return "查询成功：" + params;
    }

    //@ApiOperation(value = "管理员-预判是否存在", notes = "预判管理员是否存在")
    //@ApiJsonObject(name = "manager-checkManager", value = {
    //    @ApiJsonProperty(name = UserConst.JSON_RESULT_MESSAGE),
    //    @ApiJsonProperty(name = UserConst.JSON_USER_EMAIL)},
    //    result = @ApiJsonResult({}))
    //@ApiImplicitParam(name = "params", required = true, dataType = "manager-checkManager")
    //@ApiResponses({@ApiResponse(code = 200, message = "OK", reference = "manager-checkManager")})
    //
    //@RequestMapping(value = "/checkManager", method = RequestMethod.POST)
    //public String checkManager(@RequestBody String params)
    //{
    //    System.out.println(params);
    //    return "查询成功：" + params;
    //}
}
