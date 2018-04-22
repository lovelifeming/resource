package com.zsm.test.controller;

import com.zsm.test.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/20.
 * @Modified By:
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/test/")
public class TestController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String getRequest(String param)
    {
        System.out.println("param is " + param);
        LOGGER.info("request get test success! param is " + param);

        return "request success";
    }
}
