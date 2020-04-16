package com.zsm.controller;

import com.zsm.pojo.ResultSet;
import com.zsm.pojo.UserInfo;
import com.zsm.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/nostate/user")
public class UserController
{
    @Resource
    private UserService userService;

    @GetMapping("/userInfo")
    @ApiOperation("用户信息")
    public ResultSet<List<UserInfo>> getUserInfo()
    {
        return userService.getUserInfo();
    }
}
