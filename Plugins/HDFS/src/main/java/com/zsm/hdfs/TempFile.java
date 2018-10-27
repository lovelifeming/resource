package com.zsm.hdfs;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 按照日期写入文件
 *
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/10/18.
 * @Modified By:
 */
public class TempFile
{
    private static volatile long INDEX = -1;

    private static volatile Vector<String> TEMP = new Vector<>();

    private static String LOCAL_FOLDER = "/data/tmp/";    //System.getProperty("java.io.tmpdir");

    private static String FILE_SEPARATOR = System.getProperty("file.separator");

    public static volatile String FILE_NAME_PREFIX = "canal";

    public static final SimpleDateFormat DATE_FORMAT_SHORT = new SimpleDateFormat("yyyy-MM-dd");

    //public static String FILE_PATH = "/data/tmp/canal/";

    //public static volatile String HDFS_PATH;

    public static volatile String FILE_NAME;

    /**
     * 初始化文件路径和定时器
     */
    public static void init()
    {
        FILE_NAME = FILE_NAME_PREFIX + "-" + DATE_FORMAT_SHORT.format(new Date()) + ".txt";
        //HDFS_PATH = FILE_PATH + FILE_NAME;
        Calendar calendar = Calendar.getInstance();
        //获取下一天0时时间，0时开始启动定时器，每天定时循环
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
            0, 0, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                // 修改临时文件路径，并上传文件到HDFS
                //HDFS_PATH =HDFSUtils.HDFS_PATH + HDFSUtils.fileNamePrefix + "-" + DATE_FORMAT_SHORT.format(new Date()) + ".txt";
                //String localPath = LOCAL_FOLDER + FILE_SEPARATOR + FILE_NAME;
                boolean flag = exchangeFile();
                int index = 0;
                while (!flag)
                {
                    if (index > 3)
                    {
                        System.out.println("exchange file error!");
                        break;
                    }
                    flag = exchangeFile();
                    index++;
                }
                //File file = new File(localPath);
                //if (file.exists())
                //{
                //    try
                //    {
                //        if (HDFSUtils.mkdir(FILE_PATH))
                //            HDFSUtils.uploadLocalFileToHDFS(localPath, HDFS_PATH);
                //    }
                //    catch (IOException e)
                //    {
                //        e.printStackTrace();
                //    }
                //}
            }
        }, calendar.getTime(), 86400000);  //86400000       3600000
    }

    public static synchronized boolean append(String content)
        throws IOException
    {
        //先存入临时缓存中，再写入到本地临时文件中
        TEMP.add(content);
        INDEX++;
        if (INDEX > 200)
        {
            flush();
        }
        return true;
    }

    private static synchronized boolean exchangeFile()
    {
        String fileName = FILE_NAME_PREFIX + "-" + DATE_FORMAT_SHORT.format(new Date()) + ".txt";
        String localPath = LOCAL_FOLDER + FILE_SEPARATOR + FILE_NAME;
        File local = new File(localPath);
        if (!local.exists())
        {
            try
            {
                local.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return false;
            }
        }
        FILE_NAME = fileName;
        return true;
    }

    public static synchronized void flush()
        throws IOException
    {
        BufferedWriter bw = null;
        try
        {
            File file = new File(LOCAL_FOLDER + FILE_SEPARATOR + FILE_NAME);
            if (!file.getParentFile().exists() || !file.exists())
            {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            for (int i = 0; i < TEMP.size(); i++)
            {
                bw.write(TEMP.get(i));
                bw.newLine();
            }
            TEMP.clear();
            INDEX = -1;
            bw.flush();
        }
        finally
        {
            if (null != bw)
                bw.close();
        }
    }
}
