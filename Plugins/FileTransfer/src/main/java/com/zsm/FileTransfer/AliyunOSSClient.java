package com.zsm.FileTransfer;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.StringUtils;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * 连接OSS阿里云客户端,bucket是在网页端手动创建的，FOLDER是bucket下面的文件夹路径
 */
public class AliyunOSSClient
{
    private final Log LOG = LogFactory.getLog(AliyunOSSClient.class);

    private OSSClient ossClient;

    // 阿里云API的内或外网域名
    private String ENDPOINT;

    // 阿里云API的密钥Access Key ID
    private String ACCESS_KEY_ID;

    // 阿里云API的密钥Access Key Secret
    private String ACCESS_KEY_SECRET;

    // 阿里云API的bucket名称
    private String BACKET_NAME;

    // 阿里云API的文件夹名称
    private String FOLDER;

    private static Map<String, String> FILE_TYPE = new HashMap<>();

    // 初始化属性
    private void init()
    {
        Properties pro = new Properties();
        Reader reader = null;
        try
        {
            String resource = "aliyun_oss.properties";
            reader = Resources.getResourceAsReader(resource);
            pro.load(reader);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOG.info(e.getMessage());
        }
        finally
        {
            Utils.closeStream(reader);
        }
        ENDPOINT = pro.getProperty("OSS.ENDPOINT");
        ACCESS_KEY_ID = pro.getProperty("OSS.ACCESS_KEY_ID");
        ACCESS_KEY_SECRET = pro.getProperty("OSS.ACCESS_KEY_SECRET");
        BACKET_NAME = pro.getProperty("OSS.BACKET_NAME");
        FOLDER = pro.getProperty("OSS.FOLDER");
    }

    /**
     * @param bucketName 存储空间名
     * @param folder     存储空间名下面的文件夹
     * @param sourcePath 上传文件全路径
     * @param targetPath 下载文件存储全路径
     * @return
     */
    public static String ossUploadDown(String bucketName, String folder, String sourcePath, String targetPath)
    {
        AliyunOSSClient aliyunOSSClient = new AliyunOSSClient();
        aliyunOSSClient.createFolder(bucketName, folder);
        File file = new File(sourcePath);
        String url = aliyunOSSClient.uploadObject2OSS(file, folder + "/");
        aliyunOSSClient.downOssFile(targetPath, url);
        return aliyunOSSClient.getUrl(file.getName());
    }

    public AliyunOSSClient()
    {
        init();
        ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 获取OSSClient客户端
     *
     * @return
     */
    public OSSClient getOSSClient()
    {
        return ossClient;
    }

    /**
     * 创建Bucket存储空间
     *
     * @param bucketName 存储空间
     * @return
     */
    public String createBucketName(String bucketName)
    {
        if (!ossClient.doesBucketExist(bucketName))
        {
            // 创建存储空间
            Bucket bucket = ossClient.createBucket(bucketName);
            LOG.info("创建存储空间成功: " + bucketName);
            return bucket.getName();
        }
        return bucketName;
    }

    /**
     * 删除Bucket存储空间
     *
     * @param bucketName
     */
    public void deleteBucket(String bucketName)
    {
        ossClient.deleteBucket(bucketName);
        LOG.info("成功删除Bucket：" + bucketName);
    }

    /**
     * 创建存储文件夹
     *
     * @param bucketName
     * @param folder
     * @return
     */
    public String createFolder(String bucketName, String folder)
    {
        // 判断文件夹是否存在，不存在则创建
        if (!ossClient.doesObjectExist(bucketName, folder))
        {
            // 创建文件夹
            ossClient.putObject(bucketName, folder, new ByteArrayInputStream(new byte[0]));
            LOG.info("文件夹创建成功：" + folder);
            // 得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, folder);
            return object.getKey();
        }
        return folder;
    }

    /**
     * 根据key删除OSS服务器上的文件
     *
     * @param bucketName 存储空间
     * @param folder     文件夹名
     * @param fileName   Bucket下的文件的路径名+文件名
     */
    public void deleteFile(String bucketName, String folder, String fileName)
    {
        ossClient.deleteObject(bucketName, folder + fileName);
        LOG.info("成功删除：" + bucketName + " " + folder + " " + fileName);
    }

    /**
     * 上传图片至OSS
     *
     * @param file   上传文件全路径
     * @param folder 上传文件存储文件夹
     * @return 返回OSS内部文件存储路径
     */
    public String uploadObject2OSS(File file, String folder)
    {
        String result = "";
        // 以输入流的形式上传文件
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(file);
            // 文件名
            String fileName = file.getName();
            // 文件大小
            Long fileSize = file.length();
            // 创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            // 上传的文件的长度
            metadata.setContentLength(inputStream.available());
            // 指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            // 指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            // 指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            // 如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");

            String path = FOLDER + folder + fileName;
            // 上传文件 (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(BACKET_NAME, path, inputStream, metadata);
            // 解析结果
            result = path;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            LOG.info(e.getMessage());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOG.info(e.getMessage());
        }
        return result;
    }

    /**
     * 根据文件名称获得url链接，浏览器直接查看链接
     *
     * @param fileUrl
     * @return
     */
    public String getImgUrl(String fileUrl)
    {
        if (!StringUtils.isNullOrEmpty(fileUrl))
        {
            String[] split = fileUrl.split("/");
            return getUrl(FOLDER + split[split.length - 1]);
        }
        return null;
    }

    /**
     * 根据文件名称获得url链接，浏览器直接查看链接
     *
     * @param fileName 文件名称
     * @return
     */
    public String getUrl(String fileName)
    {
        // 设置URL过期时间为1年 1000*36000*24*365
        Date expiration = new Date(new Date().getTime() + (1000 * 36000 * 24 * 365));
        URL url = ossClient.generatePresignedUrl(BACKET_NAME, fileName, expiration);
        if (url != null)
        {
            return url.toString();
        }
        return null;
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public String getContentType(String fileName)
    {
        // 文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (FILE_TYPE.containsKey(fileExtension))
        {
            return FILE_TYPE.get(fileExtension);
        }
        // 默认返回类型
        return "image/jpeg";
    }

    /**
     * Oss文件下载
     *
     * @param filePath
     * @param ossPath
     */
    public void downOssFile(String filePath, String ossPath)
    {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try
        {
            bis = new BufferedInputStream(getOSS2InputStream(BACKET_NAME, ossPath));
            File file = new File(filePath);
            if (!file.exists())
            {
                file.createNewFile();
            }
            bos = new BufferedOutputStream(new FileOutputStream(file));
            int temp = 0;
            while ((temp = bis.read()) != -1)
            {
                bos.write(temp);
            }
            bos.flush();
            LOG.info("文件获取成功: " + filePath);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            LOG.info(e.getMessage());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOG.info(e.getMessage());
        }
        finally
        {
            Utils.closeStream(bis, bos);
        }
    }

    public final InputStream getOSS2InputStream(String bucketName, String filePath)
    {
        OSSObject ossObj = ossClient.getObject(bucketName, filePath);
        return ossObj.getObjectContent();
    }

    static
    {
        FILE_TYPE.put(".bmp", "image/bmp");
        FILE_TYPE.put(".gif", "image/gif");
        FILE_TYPE.put(".jpeg", "image/jpeg");
        FILE_TYPE.put(".jpg", "image/jpeg");
        FILE_TYPE.put(".png", "image/jpeg");
        FILE_TYPE.put(".html", "text/html");
        FILE_TYPE.put(".txt", "text/plain");
        FILE_TYPE.put(".vsd", "application/vnd.visio");
        FILE_TYPE.put(".ppt", "application/vnd.ms-powerpoint");
        FILE_TYPE.put(".pptx", "application/vnd.ms-powerpoint");
        FILE_TYPE.put(".doc", "application/msword");
        FILE_TYPE.put(".docx", "application/msword");
        FILE_TYPE.put(".xml", "text/xml");
        FILE_TYPE.put(".mp4", "video/mp4");
    }
}
