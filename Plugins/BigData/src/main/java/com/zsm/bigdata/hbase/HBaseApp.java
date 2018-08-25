package com.zsm.bigdata.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

import java.io.IOException;


/**
 * HBase可以随机的、实时的访问大数据
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/2/8 14:51.
 * @Modified By:
 */
public class HBaseApp
{
    /**
     * HBase调用MapReduce操作数据
     *
     * @param args 输入路径，job名称，表名
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public static boolean hbaseMapReducer(String[] args)
        throws IOException, ClassNotFoundException, InterruptedException
    {
        if (args.length != 3)
        {
            return false;
        }
        String inputPath = args[0];
        String jobName = args[1];
        String tableName = args[2];
        // 创建数据表
        createHBaseTable(tableName);
        // 配置MapReduce或者将hadoop和hbase的相关配置文件引入项目
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "localhost:9000");
        conf.set("mapred.job.tracker", "localhost:9001");
        conf.set("hbase.zookeeper.quorum", "localhost");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set(TableOutputFormat.OUTPUT_TABLE, tableName);
        //创建job,设置job的名称
        Job job = Job.getInstance(conf, jobName);
        //设置job处理类，就是主类
        job.setJarByClass(HBaseApp.class);
        //设置map相关的
        job.setMapperClass(HBaseMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        //设置reduce相关的
        job.setReducerClass(HBaseReducer.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TableOutputFormat.class);
        //设置任务处理的输出路径
        FileInputFormat.addInputPath(job, new Path(inputPath));
        //执行MR任务
        boolean result = job.waitForCompletion(true);
        return result;
    }

    public static void createHBaseTable(String tableName)
        throws IOException
    {
        // 配置HBse
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "localhost");
        conf.set("hbase.zookeeper.property.clientPort", "2181");

        Connection conn = ConnectionFactory.createConnection(conf);
        HBaseAdmin admin = (HBaseAdmin)conn.getAdmin();
        if (admin.tableExists(TableName.valueOf(tableName)))
        {
            admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(TableName.valueOf(tableName));
        }
        // 创建表描述
        HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
        // 在表描述里添加列族
        tableDesc.addFamily(new HColumnDescriptor("columnFamily"));
        admin.createTable(tableDesc);
    }

    public static class HBaseMapper extends Mapper<IntWritable, Text, Text, IntWritable>
    {
        private final static IntWritable NUMBER_ONE = new IntWritable(1);

        private Text rowKey = new Text();

        @Override
        protected void map(IntWritable key, Text value, Context context)
            throws IOException, InterruptedException
        {
            String[] elements = value.toString().split(" ");
            for (String element : elements)
            {
                rowKey.set(element);
                context.write(rowKey, NUMBER_ONE);
            }
        }
    }


    public static class HBaseReducer extends TableReducer<Text, IntWritable, NullWritable>
    {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException
        {
            int sum = 0;
            for (IntWritable value : values)
            {
                sum += value.get();
            }
            // Put实例化，每个rowKey存一行
            Put put = new Put(key.getBytes());
            // 列族为columnFamily,列名为columnName,列值为所有行值的操作结果
            put.addColumn("columnFamily".getBytes(), "columnName".getBytes(), String.valueOf(sum).getBytes());
            context.write(NullWritable.get(), put);
        }
    }

}
