package com.zsm.FileTransfer;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/3/22.
 * @Modified By:
 */
public class HDFSFileOperator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HDFSFileOperator.class);

    private static final int BUFFER_SIZE = 4096;

    //服务器上运行不带IP地址，使用   "";     "hdfs://127.0.0.1:8020";
    private static String HDFS_URI = "hdfs://127.0.0.1:8020";

    private static Configuration configuration = new Configuration();

    /**
     * 上传文件
     *
     * @param sourcePath
     * @param targetPath
     * @return
     */
    public static boolean upLoad(String sourcePath, String targetPath)
    {
        FileInputStream fis = null;
        FSDataOutputStream os = null;
        try
        {
            fis = new FileInputStream(new File(sourcePath));
            FileSystem fs = getFileSystem(targetPath);
            os = fs.create(new Path(targetPath));
            //上传文件
            IOUtils.copyBytes(fis, os, BUFFER_SIZE, true);
            //fs.copyFromLocalFile(new Path(sourcePath), new Path(targetPath));
            return true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            Utils.closeStream(fis, os);
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param sourcePath
     * @param targetPath
     * @return
     */
    public static boolean downLoad(String sourcePath, String targetPath)
    {
        try
        {
            return downLoad(sourcePath, new FileOutputStream(new File(targetPath)));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param sourcePath
     * @param outputStream
     * @return
     */
    public static boolean downLoad(String sourcePath, OutputStream outputStream)
    {
        FileSystem fs = getFileSystem(sourcePath);
        try
        {
            InputStream is = fs.open(new Path(HDFS_URI + sourcePath));

            IOUtils.copyBytes(is, outputStream, BUFFER_SIZE, true);
            //fs.copyToLocalFile(true, sourcePath, targetPath);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        return false;
    }

    /**
     * 删除文件或者文件夹
     *
     * @param path
     * @return
     */
    public static boolean rmdirFileORDir(String path)
    {
        FileSystem fs = getFileSystem();
        if (StringUtils.isNotBlank(HDFS_URI))
        {
            path = HDFS_URI + path;
        }
        try
        {
            return fs.delete(new Path(path), true);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        finally
        {
            Utils.closeStream(fs);
        }
        return false;
    }

    /**
     * 获取文件系统，无文件夹目录
     *
     * @return FileSystem 文件系统
     */
    public static FileSystem getFileSystem()
    {
        return getFileSystem("");
    }

    /**
     * 获取文件系统
     *
     * @param filePath 上传文件夹路径
     * @return FileSystem 文件系统
     */
    public static FileSystem getFileSystem(String filePath)
    {
        // 文件系统
        FileSystem fs = null;
        try
        {
            if (StringUtils.isBlank(HDFS_URI))
            {
                // 返回默认文件系统  如果在 Hadoop集群下运行，使用此种方法可直接获取默认文件系统
                fs = FileSystem.get(configuration);
            }
            else
            {
                // 返回指定的文件系统,如果在本地测试，需要使用此种方法获取文件系统
                URI url = new URI(HDFS_URI.trim() + "/" + filePath);
                fs = FileSystem.get(url, configuration);
            }
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        return fs;
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @return
     */
    public static boolean createFile(String filePath)
    {
        FileSystem fs = getFileSystem();
        FSDataOutputStream outputStream = null;
        try
        {
            outputStream = fs.create(new Path(filePath));
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        finally
        {
            Utils.closeStream(outputStream);
        }
        return false;
    }

    /**
     * 创建文件夹
     *
     * @param directory
     * @return
     */
    public static boolean mkdirsDirectory(String directory)
    {
        FileSystem fs = getFileSystem();
        Path path = new Path(directory);
        try
        {
            if (fs.exists(path))
            {
                return true;
            }
            return fs.mkdirs(path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        return false;
    }

    /**
     * 检查文件或文件夹是否存在
     *
     * @param filePath
     * @return true 存在 false 不存在
     */
    public static boolean checkFileOrDir(String filePath)
    {
        FileSystem fs = getFileSystem();
        try
        {
            return fs.exists(new Path(filePath));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        return false;
    }

    /**
     * 判断文件夹是否存在
     *
     * @param filePath
     * @return true 存在 false 不存在
     */
    public static boolean checkDirectory(String filePath)
    {
        return checkDirectory(filePath, false);
    }

    /**
     * 判断文件夹是否存在
     *
     * @param filePath 文件夹路径
     * @param create   如果文件夹不存在，是否创建文件夹
     * @return true 存在 false 不存在
     */
    public static boolean checkDirectory(String filePath, boolean create)
    {
        if (StringUtils.isBlank(filePath))
        {
            return false;
        }
        boolean flag = false;
        FileSystem fs = getFileSystem();
        Path path = new Path(filePath);
        try
        {
            if (fs.exists(path))
            {
                flag = fs.isDirectory(path);
            }
            else
            {
                if (create)
                {
                    flag = fs.mkdirs(path);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        return flag;
    }
}
