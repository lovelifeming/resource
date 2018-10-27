package com.zsm.hdfs;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


/**
 * HDFS 操作类
 *
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/10/10.
 * @Modified By:
 */
public class HDFSUtils
{
    private static String URL = "hdfs://hadoop1:8020";

    private static Configuration getConfig(String name, String value, String name1, String value1,
                                           String name2, String value3)
    {
        Configuration conf = new Configuration();
        //conf.set("fs.defaultFS", "hdfs://127.0.0.1:9000");
        conf.set(name, value);
        conf.set(name1, value1);
        conf.set(name2, value3);
        return conf;
    }

    public static FileSystem getFileSystem()
        throws IOException
    {
        Configuration conf = getConfig("fs.defaultFS", URL,
            "fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem",
            "dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        //在服务器获取本地HDFS文件系统访问实例
        FileSystem fs = FileSystem.get(conf);
        return fs;
    }

    public static FileSystem getFileSystem(String url)
        throws IOException
    {
        Configuration conf = getConfig("dfs.nameservices", "hadoop1", "dfs.ha.namenodes.hadoop1",
            "nn1,nn2", "dfs.namenode.rpc-address.hadoop1.nn1", "127.0.0.1:8020");
        conf.set("dfs.namenode.rpc-address.hadoop1.nn2", "127.0.0.1:8020");
        conf.set("dfs.client.failover.proxy.provider.testcluster",
            "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        conf.setInt("dfs.client.socket-timeout", 3600000);
        FileSystem fs = FileSystem.get(new Path(url).toUri(), conf);
        return fs;
    }

    public static boolean mkdir(String dir)
        throws IOException
    {
        if (StringUtils.isBlank(dir))
        {
            return false;
        }
        dir = URL + dir;
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dir), conf);
        if (!fs.exists(new Path(dir)))
        {
            fs.mkdirs(new Path(dir));
        }
        fs.close();
        return true;
    }

    public static boolean deleteDir(String dir)
        throws IOException
    {
        if (StringUtils.isBlank(dir))
        {
            return false;
        }
        dir = URL + dir;
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dir), conf);
        fs.delete(new Path(dir), true);
        fs.close();
        return true;
    }

    public static List<String> listAll(String dir)
        throws IOException
    {
        if (StringUtils.isBlank(dir))
        {
            return new ArrayList<String>();
        }
        dir = URL + dir;
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dir), conf);
        FileStatus[] stats = fs.listStatus(new Path(dir));
        List<String> names = new ArrayList<String>();
        for (int i = 0; i < stats.length; ++i)
        {
            if (stats[i].isFile())
            {
                // regular file
                names.add(stats[i].getPath().toString());
            }
            else if (stats[i].isDirectory())
            {
                // dir
                names.add(stats[i].getPath().toString());
            }
            else if (stats[i].isSymlink())
            {
                // is s symlink in linux
                names.add(stats[i].getPath().toString());
            }
        }
        fs.close();
        return names;
    }

    public static boolean uploadLocalFileHDFS(String localFile, String hdfsFile)
        throws IOException
    {
        if (StringUtils.isBlank(localFile) || StringUtils.isBlank(hdfsFile))
        {
            return false;
        }
        hdfsFile = URL + hdfsFile;
        Configuration config = new Configuration();
        FileSystem hdfs = FileSystem.get(URI.create(URL), config);
        Path src = new Path(localFile);
        Path dst = new Path(hdfsFile);
        hdfs.copyFromLocalFile(src, dst);
        hdfs.close();
        return true;
    }

    public static boolean uploadLocalFileToHDFS(String localFile, String hdfsFile)
    {
        if (StringUtils.isEmpty(localFile) || StringUtils.isEmpty(hdfsFile))
        {
            return false;
        }
        hdfsFile = URL + hdfsFile;
        Configuration conf = getConfig("fs.defaultFS", URL, "fs.hdfs.impl",
            org.apache.hadoop.hdfs.DistributedFileSystem.class.getName(),
            "fs.file.impl", LocalFileSystem.class.getName());
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        FileSystem fs = null;
        try
        {
            fs = FileSystem.get(URI.create(URL), conf);
            Path src = new Path(localFile);
            Path dst = new Path(hdfsFile);
            fs.copyFromLocalFile(src, dst);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != fs)
                try
                {
                    fs.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
        return true;
    }

    public static boolean createNewHDFSFile(String newFile, String content)
        throws IOException
    {
        if (StringUtils.isBlank(newFile) || null == content)
        {
            return false;
        }
        newFile = URL + newFile;
        Configuration config = new Configuration();
        FileSystem hdfs = FileSystem.get(URI.create(newFile), config);
        FSDataOutputStream os = hdfs.create(new Path(newFile));
        os.write(content.getBytes("UTF-8"));
        os.close();
        hdfs.close();
        return true;
    }

    public static boolean deleteHDFSFile(String hdfsFile)
        throws IOException
    {
        hdfsFile = URL + hdfsFile;
        Configuration config = new Configuration();
        FileSystem hdfs = FileSystem.get(URI.create(hdfsFile), config);
        Path path = new Path(hdfsFile);
        boolean isDeleted = hdfs.delete(path, true);
        hdfs.close();
        return isDeleted;
    }

    public static byte[] readHDFSFile(String hdfsFile)
        throws Exception
    {
        hdfsFile = URL + hdfsFile;
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(hdfsFile), conf);
        // check if the file exists
        Path path = new Path(hdfsFile);
        if (fs.exists(path))
        {
            FSDataInputStream is = fs.open(path);
            // get the file info to create the buffer
            FileStatus stat = fs.getFileStatus(path);
            // create the buffer
            byte[] buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
            is.readFully(0, buffer);
            is.close();
            fs.close();
            return buffer;
        }
        else
        {
            throw new Exception("the file is not found .");
        }
    }

    public static boolean append(String hdfsFile, String content)
        throws Exception
    {
        hdfsFile = URL + hdfsFile;
        Configuration conf = new Configuration();
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");

        FileSystem fs = FileSystem.get(URI.create(hdfsFile), conf);
        Path path = new Path(hdfsFile);
        if (fs.exists(path))
        {
            try
            {
                InputStream in = new ByteArrayInputStream(content.getBytes());
                OutputStream out = fs.append(new Path(hdfsFile));
                IOUtils.copyBytes(in, out, 4096, true);
                out.close();
                in.close();
                fs.close();
            }
            catch (Exception ex)
            {
                fs.close();
                throw ex;
            }
        }
        else
        {
            HDFSUtils.createNewHDFSFile(hdfsFile, content);
        }
        return true;
    }

    public static String getURL()
    {
        return URL;
    }

    public static void setURL(String URL)
    {
        HDFSUtils.URL = URL;
    }
}
