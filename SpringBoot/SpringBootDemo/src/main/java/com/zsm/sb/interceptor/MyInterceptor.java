package com.zsm.sb.interceptor;

import com.zsm.sb.model.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/28 17:43.
 * @Modified By:
 */
public class MyInterceptor implements HandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception
    {
        boolean flag;
        User user = (User)request.getSession().getAttribute("user");
        if (null == user)
        {
            response.sendRedirect("toLogin");
            flag = false;
        }
        else
        {
            flag = true;
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView)
        throws Exception
    {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception
    {

    }
}
