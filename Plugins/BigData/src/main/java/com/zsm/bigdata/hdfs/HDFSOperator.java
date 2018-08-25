package com.zsm.bigdata.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/8/1.
 * @Modified By:
 */
public class HDFSOperator
{
    private Configuration conf;

    private FileSystem fs;

    public void initDefaultConfig()
        throws IOException
    {
        conf = new Configuration();  //获取当前的默认环境配置
        fs = FileSystem.get(conf);   //根据当前环境配置，获取文件系统访问实例
    }

    public void initConfig()
        throws IOException
    {
        conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://127.0.0.1:9000");
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");

//        conf.set("dfs.nameservices", "qahadoop1"); //假设nameservices命名为hadoop01
//        conf.set("dfs.ha.namenodes.qahadoop1", "nn1,nn2");
//        conf.set("dfs.namenode.rpc-address.qahadoop1.nn1", "127.0.0.1:8020");
//        conf.set("dfs.namenode.rpc-address.qahadoop1.nn2", "127.0.0.1:8020");
//        conf.set("dfs.client.failover.proxy.provider.testcluster",
//            "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        fs = FileSystem.get(conf);
    }

    public void getAllFile()
    {
        String name = fs.getHomeDirectory().getName();
        System.out.println(name);
    }

}
