package com.zsm.FileTransfer;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;


public class FastDFSClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FastDFSClient.class);

    private StorageClient1 storageClient = null;

    public FastDFSClient(String conf)
        throws Exception
    {
        if (conf.contains("classpath:"))
        {
            String str = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            String path = URLDecoder.decode(str, "UTF-8");
            path = path.substring(6);
            conf = conf.replace("classpath:", URLDecoder.decode(path, "UTF-8"));
        }
        ClientGlobal.init(conf);
        storageClient = new StorageClient1(new TrackerClient().getConnection(), null);
    }

    /**
     * 上传文件方法
     *
     * @param fileName 文件全路径
     * @param extName  文件扩展名，不包含（.）
     * @param metas    文件扩展信息
     * @return
     */
    public String uploadFile(String fileName, String extName, NameValuePair[] metas)
    {
        try
        {
            return storageClient.upload_file1(fileName, extName, metas);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        catch (MyException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    /**
     * 上传文件方法
     *
     * @param fileContent 文件的内容，字节数组
     * @param extName     文件扩展名
     * @param metas       文件扩展信息
     * @return
     */
    public String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas)
    {
        try
        {
            return storageClient.upload_file1(fileContent, extName, metas);
        }
        catch (IOException e)
        {
            LOGGER.info(e.getMessage());
            e.printStackTrace();
        }
        catch (MyException e)
        {
            LOGGER.info(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传文件
     *
     * @param filePath 文件的磁盘路径名称 如：D:/image/test.jpg
     * @return null为失败
     */
    public String uploadFile(String filePath)
    {
        return uploadFile(filePath, null, null);
    }

    /**
     * 上传文件
     *
     * @param filePath 文件的磁盘路径名称 如：D:/image/test.jpg
     * @param extName  文件的扩展名 如 txt jpg等
     * @return null为失败
     */
    public String uploadFile(String filePath, String extName)
    {
        return uploadFile(filePath, extName, null);
    }

    /**
     * 上传文件
     *
     * @param fileContent 文件的字节数组
     * @return null为失败
     */
    public String uploadFile(byte[] fileContent)
    {
        return uploadFile(fileContent, null, null);
    }

    /**
     * 上传文件
     *
     * @param fileContent 文件的字节数组
     * @param extName     文件的扩展名 如 txt  jpg png 等
     * @return null为失败
     */
    public String uploadFile(byte[] fileContent, String extName)
    {
        return uploadFile(fileContent, extName, null);
    }

    /**
     * 文件下载到磁盘
     *
     * @param filePath     图片路径
     * @param outputStream 输出流 中包含要输出到磁盘的路径
     */
    public boolean download_file(String filePath, FileOutputStream outputStream)
    {
        try
        {
            byte[] b = storageClient.download_file1(filePath);
            outputStream.write(b);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        catch (MyException e)
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
     * 获取文件内容数组
     *
     * @param filePath 文件的路径 如group1/M00/00/00/d536cc757f7948b27a28426117edaa.jpg
     * @return
     */
    public byte[] download_bytes(String filePath)
    {
        try
        {
            return storageClient.download_file1(filePath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        catch (MyException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param group       组名 如：group1
     * @param storagePath 不带组名的路径名称 如：M00/00/00/d536cc757f7948b27a28426117edaa.jpg
     * @return -1失败,0成功
     */
    public int delete_file(String group, String storagePath)
    {
        try
        {
            return storageClient.delete_file(group, storagePath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        catch (MyException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        return -1;
    }

    /**
     * 删除文件
     *
     * @param groupPath 文件的全部路径 如：group1/M00/00/00/d536cc757f7948b27a28426117edaa.jpg
     * @return -1失败,0成功
     */
    public int delete_file(String groupPath)
    {
        try
        {
            return storageClient.delete_file1(groupPath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        catch (MyException e)
        {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        return -1;
    }
}
