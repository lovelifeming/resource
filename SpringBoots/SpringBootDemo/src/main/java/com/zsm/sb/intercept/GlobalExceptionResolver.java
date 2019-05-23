package com.zsm.sb.intercept;

import com.zsm.sb.model.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;


/**
 * @Author :zengsm.
 * @Description :
 * @Date:Created in 2019/5/22 19:53.
 * @Modified By :
 */
@ControllerAdvice
public class GlobalExceptionResolver implements HandlerExceptionResolver
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Nullable
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                         Object o, Exception e)
    {
        LOGGER.error(e.getMessage());
        e.printStackTrace();
        ModelAndView view = wrapperException(e);
        return view;
    }

    private ModelAndView wrapperException(Exception e)
    {
        ModelAndView view = new ModelAndView();
        HashMap<String, Object> attributes = new HashMap<>(1);
        //空指针
        if (e instanceof NullPointerException)
        {
            attributes.put("code", StatusCode.NullPointerException.getCode());
            attributes.put("description", StatusCode.NullPointerException.getMessage());
        }
        //数学运算异常--分母为零
        else if (e instanceof ArithmeticException)
        {
            attributes.put("code", StatusCode.ArithmeticException.getCode());
            attributes.put("description", StatusCode.ArithmeticException.getMessage());
        }
        //数组下标越界
        else if (e instanceof ArrayIndexOutOfBoundsException)
        {
            attributes.put("code", StatusCode.ArrayIndexOutOfBoundsException.getCode());
            attributes.put("description", StatusCode.ArrayIndexOutOfBoundsException.getMessage());
        }
        //数据操作异常
        else if (e instanceof DataAccessException)
        {
            attributes.put("code", StatusCode.DataAccessException.getCode());
            attributes.put("description", StatusCode.DataAccessException.getMessage());
        }
        //数据库异常
        else if (e instanceof SQLException)
        {
            attributes.put("code", StatusCode.SQLException.getCode());
            attributes.put("description", StatusCode.SQLException.getMessage());
        }
        //IO异常
        else if (e instanceof IOException)
        {
            attributes.put("code", StatusCode.IOException.getCode());
            attributes.put("description", StatusCode.IOException.getMessage());
        }
        //指定类不存在
        else if (e instanceof ClassNotFoundException)
        {
            attributes.put("code", StatusCode.ClassNotFoundException.getCode());
            attributes.put("description", StatusCode.ClassNotFoundException.getMessage());
        }
        //字符串转数字错误
        else if (e instanceof NumberFormatException)
        {
            attributes.put("code", StatusCode.NumberFormatException.getCode());
            attributes.put("description", StatusCode.NumberFormatException.getMessage());
        }
        //参数错误
        else if (e instanceof IllegalArgumentException)
        {
            attributes.put("code", StatusCode.IllegalArgumentException.getCode());
            attributes.put("description", StatusCode.IllegalArgumentException.getMessage());
        }
        //没有该类的访问权限
        else if (e instanceof IllegalAccessException)
        {
            attributes.put("code", StatusCode.IllegalAccessException.getCode());
            attributes.put("description", StatusCode.IllegalAccessException.getMessage());
        }
        //数据类型转换异常
        else if (e instanceof ClassCastException)
        {
            attributes.put("code", StatusCode.ClassCastException.getCode());
            attributes.put("description", StatusCode.ClassCastException.getMessage());
        }
        //数组存储异常
        else if (e instanceof ArrayStoreException)
        {
            attributes.put("code", StatusCode.ArrayStoreException.getCode());
            attributes.put("description", StatusCode.ArrayStoreException.getMessage());
        }
        //文件未找到
        else if (e instanceof FileNotFoundException)
        {
            attributes.put("code", StatusCode.FileNotFoundException.getCode());
            attributes.put("description", StatusCode.FileNotFoundException.getMessage());
        }
        //文件已结束
        else if (e instanceof EOFException)
        {
            attributes.put("code", StatusCode.EOFException.getCode());
            attributes.put("description", StatusCode.EOFException.getMessage());
        }
        //违背安全原则
        else if (e instanceof SecurityException)
        {
            attributes.put("code", StatusCode.SecurityException.getCode());
            attributes.put("description", StatusCode.SecurityException.getMessage());
        }
        //方法未找到
        else if (e instanceof NoSuchMethodException)
        {
            attributes.put("code", StatusCode.NoSuchMethodException.getCode());
            attributes.put("description", StatusCode.NoSuchMethodException.getMessage());
        }
        //线程被中断
        else if (e instanceof InterruptedException)
        {
            attributes.put("code", StatusCode.InterruptedException.getCode());
            attributes.put("description", StatusCode.InterruptedException.getMessage());
        }
        else
        {
            attributes.put("code", StatusCode.UNKNOW_ERROR.getCode());
            attributes.put("description", StatusCode.UNKNOW_ERROR.getMessage() + e.getMessage());
        }
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        jsonView.setAttributesMap(attributes);
        view.setView(jsonView);
        return view;
    }
}
