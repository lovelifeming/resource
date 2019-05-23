package com.zsm.apidoc.controller;

import com.zsm.apidoc.apijson.model.ApiJsonObject;
import com.zsm.apidoc.apijson.model.ApiJsonProperty;
import com.zsm.apidoc.apijson.model.ApiJsonResult;
import com.zsm.apidoc.apijson.util.CommonConst;
import com.zsm.apidoc.model.GlobalString;
import io.swagger.annotations.*;
import org.checkerframework.checker.units.qual.A;
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
    //@ApiOperation(value = "测试ApiJson",tags = "tage",notes = "获取用户user信息")
    //@ApiJsonObject(name = "getUser",value = {
    //    @ApiJsonProperty(name = "user_name",description = "用户名称"),
    //    @ApiJsonProperty(name = "user_age",description = "用户年龄")},
    //result = @ApiJsonResult(type = "page",name = "data_list",value = {"user_name","user_age"}))
    //@ApiImplicitParam(name = "params",required = true,dataType ="getUser" )
    //@ApiResponses({@ApiResponse(code = 200,message = "OK",reference = "getUser")})
    //@RequestMapping(value = "getUserInfo",method= RequestMethod.GET,consumes ="UTF8",produces = "UTF8")
    //public String testApiJson(@RequestBody String params)
    //{
    //    System.out.println(params);
    //
    //    return "查询成功："+params;
    //}

    @RequestMapping(value = "/api/v1/manager")
    @RestController
    @Api(description = "管理员身份接口")
    public class ManagerController {
        @ApiOperation(value = "管理员-预判是否存在",notes ="预判管理员是否存在" )
        @ApiJsonObject(name = "manager-checkManager", value = {
            @ApiJsonProperty(name = GlobalString.JSON_USER_NAME),
            @ApiJsonProperty(name = GlobalString.JSON_USER_EMAIL)},
            result = @ApiJsonResult({}))
        @ApiImplicitParam(name = "params", required = true, dataType = "manager-checkManager")
        @ApiResponses({@ApiResponse(code = 200, message = "OK", reference = "manager-checkManager")})

        @RequestMapping(value = "/checkManager", method = RequestMethod.POST)
        public String checkManager(@RequestBody String params) {
            return "";
        }
    }
}
