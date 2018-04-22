package com.zsm.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/3/15.
 * @Modified By:
 */
public class Main
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)
        throws Exception
    {
        Main main = new Main();
        main.showPath();
    }

    private void showPath()
    {
        //  file:/opt/rh/StartUpJar.jar!/com/zsm/test/
        String path = Main.class.getResource("").getPath();
        // /opt/rh/StartUpJar.jar
        String path1 = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        // /opt/rh/StartUpJar.jar
        String path2 = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        // 获取相对路径下路的资源文件 file:/opt/rh/StartUpJar.jar!/com/zsm/test/
        String path3 = this.getClass().getResource("").getPath();
        //  StartUpJar.jar
        String path4 = System.getProperty("java.class.path");
        // /opt/rh
        String path5 = System.getProperty("user.dir");
        // /opt/rh
        String path6 = new File("").getAbsolutePath();

        LOGGER.info(path);
        LOGGER.info(path1);
        LOGGER.info(path2);
        LOGGER.info(path3);
        LOGGER.info(path4);
        LOGGER.info(path5);
        LOGGER.info(path6);
    }
}
