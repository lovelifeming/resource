package com.zsm.sb.controller;

import com.zsm.sb.model.ReturnMsg;
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
    @Autowired
    private StudentController studentController;

    @Test
    public void crossDomainSetCookie()
    {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Origin")).thenReturn("127.0.0.1");
        Cookie[] cookies = {new Cookie("Login", "true")};
        Mockito.when(request.getCookies()).thenReturn(cookies);
        HttpSession session = Mockito.mock(HttpSession.class);
        Enumeration<String> en = Mockito.mock(Enumeration.class);
        Mockito.when(en.hasMoreElements()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(en.nextElement()).thenReturn("test").thenReturn("test1");
        Mockito.when(session.getAttributeNames()).thenReturn(en);
        Mockito.when(request.getSession()).thenReturn(session);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(response.getStatus()).thenReturn(1);
        String value = "e10adc3949ba59abbe56e057f20f883e";
        ReturnMsg msg = studentController.crossDomainSetCookie(request, response, "test", value);

    }
}