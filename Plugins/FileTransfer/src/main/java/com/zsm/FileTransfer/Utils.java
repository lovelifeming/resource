package com.zsm.FileTransfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

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
                LOGGER.info(e.getMessage());
            }
        }
    }
}
