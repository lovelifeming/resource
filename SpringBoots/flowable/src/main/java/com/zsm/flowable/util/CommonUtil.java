package com.zsm.flowable.util;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-18 13:59.
 * @Description:
 */
public class CommonUtil
{
    /**
     * 默认转换为 yyyy-MM-dd HH:mm:ss 格式
     */
    public static String convertDate(Date source)
    {
        return convertDate(source, "yyyy-MM-dd HH:mm:ss");
    }

    public static String convertDate(Date source, String dateFormat)
    {
        if (source == null || dateFormat == null || "".equals(dateFormat.trim()))
        {
            return "";
        }
        else
        {
            synchronized (CommonUtil.class)
            {
                try
                {
                    SimpleDateFormat df = new SimpleDateFormat(dateFormat);
                    return df.format(source);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return "";
                }
            }
        }
    }
}
