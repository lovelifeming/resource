package com.zsm.sb.interceptor;

import org.slf4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/16.
 * @Modified By:
 */
public class CorsFilter implements Filter
{
    private final static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CorsFilter.class);

    @Override
    public void init(FilterConfig filterConfig)
        throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        //设置可跨域域名，1.具体域名地址；2. * 代替 3.request.getHeader("Origin")
        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        chain.doFilter(request, httpServletResponse);
    }

    @Override
    public void destroy()
    {

    }
}
