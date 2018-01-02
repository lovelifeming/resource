package com.zsm.sb.controller;

import com.zsm.sb.model.ConfigBean;
import com.zsm.sb.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/25 14:24.
 * @Modified By:
 */
@Controller
@EnableAutoConfiguration
@EnableConfigurationProperties({ConfigBean.class})
@SpringBootApplication
@RequestMapping("/user/")
public class SimpleController
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value(value = "${com.zsm.springboot.name}")
    private String name;

    @Autowired
    private ConfigBean configBean;

    @RequestMapping(value = "userLogin", method = RequestMethod.POST)
    @ResponseBody
    public String userLogin(User user, Model model)
    {
        System.out.println(user);
        System.out.println(name);
        System.out.println(configBean.getSecret());

        model.addAttribute("name", user.getUserName());
        model.addAttribute("password", user.getPassword());
        return "result";
    }
}
