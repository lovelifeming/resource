package com.zsm.sb.controller;

import com.alibaba.fastjson.JSONObject;
import com.zsm.sb.model.ConfigBean;
import com.zsm.sb.model.ReturnMsg;
import com.zsm.sb.model.Student;
import com.zsm.sb.service.StudentService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/10.
 * @Modified By:
 */
@RestController
@RequestMapping("/test/")
@EnableAutoConfiguration
@EnableConfigurationProperties({ConfigBean.class})
@Api("SwaggerDemoController 相关API")
public class StudentController
{
    @Autowired
    private StudentService studentService;

    @Autowired
    private ConfigBean configBean;

    @ApiOperation(value = "根据username查找", notes = "查询数据库中某个的用户信息")
    @ApiImplicitParam(name = "name", value = "用户名字", paramType = "path", required = true, dataType = "String", example = "李晓明")
    @RequestMapping("find/{name}")
    public String selectTestInfo(@PathVariable String name)
    {
        System.out.println(configBean.getName());
        return studentService.selectStudentByName(name).toString();
    }

    @RequestMapping(value = "userLogin", method = RequestMethod.POST)
    @ResponseBody
    public String userLogin(Student student, Model model)
    {
        System.out.println(student);
        System.out.println(configBean.getSecret());

        model.addAttribute("name", student.getUser_name());
        model.addAttribute("password", student.getPassword());
        return "result";
    }

    @ApiIgnore
    @RequestMapping(value = "post", method = RequestMethod.POST)
    public String postRequest(HttpServletRequest request, HttpServletResponse response)
    {
        String data = request.getParameter("data");
        JSONObject json = new JSONObject();
        json.put("data", data);
        json.put("message", "test post request success");

        return json.toString();
    }

    @ApiIgnore
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String getRequest()
    {
        return "test get request success";
    }

    @RequestMapping(value = "toLogin", method = RequestMethod.POST)
    @ResponseBody
    public String signingIn(HttpServletRequest request, HttpServletResponse response)
    {
        Student student = new Student();
        student.setUser_name(request.getParameter("userName"));
        student.setPassword(request.getParameter("password"));
        request.getSession().setAttribute("user", student);
        JSONObject json = new JSONObject();
        json.put("result", "1");

        return json.toString();
    }

    @ApiOperation(value = "根据username查找", notes = "查询数据库中某个的用户信息")
    @ApiImplicitParam(name = "name", value = "李晓明", paramType = "path", required = true, dataType = "String")
    @RequestMapping(value = "json/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnMsg userJSONInfo(@PathVariable String name)
    {
        System.out.println(configBean.getName());
        Student student = studentService.selectStudentByName(name);
        return ReturnMsg.generatorSuccessMsg(student);
    }

    @ApiOperation(value = "根据username查找", notes = "查询数据库中某个的用户信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "name", value = "李晓明", paramType = "path", required = true, dataType = "String"))
    @RequestMapping(value = "xml/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public ReturnMsg userXMLInfo(@PathVariable String name)
    {
        System.out.println(configBean.getName());
        Student student = studentService.selectStudentByName(name);
        return ReturnMsg.generatorSuccessMsg(student);
    }

    @ApiOperation(value = "获取Cookie", notes = "跨域设置Cookie")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ReturnMsg crossDomainSetCookie(HttpServletRequest request, HttpServletResponse response,
                                          String name, String value)
    {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "0");
        String allowHeaders = "Origin, No-Cache, X-Requested-With, If-Modified-Since,Pragma, Last-Modified, " +
                              "Cache-Control,Expires, Content-Type,X-E4M-With,userId,token,Access-Control-Allow-Headers";
        response.setHeader("Access-Control-Allow-Headers", allowHeaders);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed", "1");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");

        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/student");
        cookie.setDomain("zsm.com");
        cookie.setVersion(1);
        cookie.setComment("测试设置cookie");
        cookie.setSecure(true);
        response.addCookie(cookie);

        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies)
        {
            System.out.println("CookieName: " + c.getName() + "  CookieValue: " + c.getValue());
        }

        HttpSession session = request.getSession();
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements())
        {
            String element = attributeNames.nextElement();
            System.out.println(element);
        }
        try
        {
            String authType = request.getAuthType();
            System.out.println("auth type is:" + authType);
        }
        catch (Exception e)
        {
            System.out.println("exception message:" + e.getMessage());
        }

        int status = response.getStatus();
        System.out.println("the response status is:" + status);
        return ReturnMsg.generatorSuccessMsg(request.getParameter("data"));
    }

    @ApiOperation("用户信息提交")
    @RequestMapping(value = "putUserInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "studentInfo", value = "JSONObject", required = true)})
    public ReturnMsg sendKafka(@RequestBody JSONObject studentInfo)
    {
        System.out.println(studentInfo);
        return ReturnMsg.generatorSuccessMsg(studentInfo);
    }

    @ApiOperation(value = "用户列表查询", notes = "分页查询数据库中用户信息，模糊匹配")
    @RequestMapping("list/{name}/{pageNum}/{pageSize}")
    public ReturnMsg getUserInfoList(@RequestParam(value = "name") @ApiParam("模糊查询用户名") @PathVariable String name,
                                     @RequestParam(value = "pageNum", defaultValue = "1") @ApiParam("查询页编号")
                                     @PathVariable Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") @ApiParam("查询页数据条数")
                                     @PathVariable Integer pageSize)
    {
        System.out.println(configBean.getName());
        return studentService.getUserInfoList(name, pageNum, pageSize);
    }
}
