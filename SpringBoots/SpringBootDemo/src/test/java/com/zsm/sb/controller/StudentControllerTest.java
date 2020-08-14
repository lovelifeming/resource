package com.zsm.sb.controller;

import com.zsm.sb.model.ResultVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;


/**
 * @Author :zengsm.
 * @Description :
 * @Date:Created in 2019/3/11 17:17.
 * @Modified By :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentControllerTest
{
    /**
     * 自动注入
     */
    @Autowired
    private StudentController studentController;

    @Test
    public void crossDomainSetCookie()
    {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        //Mock带参方法返回值
        Mockito.when(request.getHeader("Origin")).thenReturn("127.0.0.1");
        Mockito.when(request.getParameter(any())).thenReturn("test");
        Cookie[] cookies = {new Cookie("Login", "true")};
        //设置无参方法返回值
        Mockito.when(request.getCookies()).thenReturn(cookies);
        //Mock对象实例
        HttpSession session = Mockito.mock(HttpSession.class);
        Enumeration<String> en = Mockito.mock(Enumeration.class);
        //Mock集合对象返回多个值，第一次返回值true，第二次返回值ture，第三次及以后返回false
        Mockito.when(en.hasMoreElements()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(en.nextElement()).thenReturn("login").thenReturn("loginFlag");
        Mockito.when(session.getAttributeNames()).thenReturn(en);
        Mockito.when(request.getSession()).thenReturn(session);
        //Mock方法调用时抛出异常
        doThrow(new IllegalArgumentException("nothing auth type!")).when(request).getAuthType();

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(response.getStatus()).thenReturn(1);
        String value = "e10adc3949ba59abbe56e057f20f883e";

        ResultVO msg = studentController.crossDomainSetCookie(request, response, "test", value);
        Assert.assertEquals("test", msg.getData());
    }
}