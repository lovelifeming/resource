package com.zsm.bigdata.util;

import java.io.Closeable;
import java.io.IOException;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/2/7 14:49.
 * @Modified By:
 */
public class Utils
{
    /**
     * 关闭继承于Closeable接口的流对象
     *
     * @param closeables
     */
    public static void closeStream(Closeable... closeables)
    {
        for (Closeable closeable : closeables)
        {
            try
            {
                if (closeable != null)
                {
                    closeable.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println("FileUtils-->closeStream-->" + e.getStackTrace());
            }
        }
    }
}
