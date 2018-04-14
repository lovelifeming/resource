package com.zsm.sb.controller;

import com.zsm.sb.model.ConfigBean;
import com.zsm.sb.model.Student;
import com.zsm.sb.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/11.
 * @Modified By:
 */
@RestController
@RequestMapping("/api")
@EnableConfigurationProperties({ConfigBean.class})
@Api("SwaggerDemoController 相关API")
public class SwaggerDemoController
{
    private static final Logger logger = LoggerFactory.getLogger(SwaggerDemoController.class);

    //在application.properties文件里面配置参数值
    @Value(value = "${com.zsm.springboot.name}")
    private String name;

    @Autowired
    private StudentService studentService;

    @ApiOperation(value = "根据user_no查询学生信息", notes = "查询数据库中某个的学生信息")
    @ApiImplicitParam(name = "name", value = "学生名字", paramType = "path", required = true, dataType = "String")
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public Student getStudent(@PathVariable String name)
    {
        logger.info("开始查询某个学生信息");
        return studentService.selectStudentByName(name);
    }
}
