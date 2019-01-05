package com.zsm.sb.interceptor;

import com.zsm.sb.model.Student;
import io.netty.util.internal.StringUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 拦截器
 *
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/28 17:43.
 * @Modified By:
 */
public class MyInterceptor implements HandlerInterceptor
{
    /**
     * 检查用户是否已经登陆
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception
    {
        boolean flag;
        Student user = (Student)request.getSession().getAttribute("user");
        String url = request.getServletPath();
        //排除 swagger请求
        if (url.contains("swagger"))
        {
            return true;
        }
        if (null == user || StringUtil.isNullOrEmpty(user.getUser_name()))
        {
            response.sendRedirect("/toLogin");
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
