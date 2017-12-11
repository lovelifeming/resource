package com.zsm.ssh.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.zsm.ssh.entity.User;
import com.zsm.ssh.service.UserService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/6 15:45.
 * @Modified By:
 */
@Controller("userAction")
@Results({@Result(name = "input", location = "/index.jsp", type = "redirect", params = {"error", "1"}),
    @Result(name = "success", location = "/success.jsp", type = "redirect")})
public class UserAction extends ActionSupport
{
    //Spring 注入方式
    @Autowired
    private UserService userService;

    @Action("/execute")
    public String execute()
    {
        List<User> allUser = userService.getAllUser();
        //获取Context上下文对象
        ActionContext ac = ActionContext.getContext();
        //将myBookCardList集合添加到上下文对象里
        ac.put("allUser", allUser);

        return "index";
    }
}
