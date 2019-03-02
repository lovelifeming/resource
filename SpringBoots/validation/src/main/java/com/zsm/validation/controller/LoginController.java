package com.zsm.validation.controller;

import com.zsm.validation.model.LoginVO;
import com.zsm.validation.model.RespResult;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2019/2/25 17:19.
 * @Modified By:
 */
@RestController
@RequestMapping("/login/")
public class LoginController
{
    private ObjectError error;

    @RequestMapping("index.html")
    public ModelAndView index(){
        return new ModelAndView("login");
    }

    @PostMapping("login.jsp")
    public RespResult login(@Valid LoginVO loginVO, BindingResult bindingResult, Model mode)
    {
        if(bindingResult.hasErrors())
        {
            for (ObjectError error : bindingResult.getAllErrors())
            {
                return RespResult.failure(error.getDefaultMessage());
            }
        }
        return RespResult.success("验证成功");

    }



}
