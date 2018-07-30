package com.zsm.bigdata.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles;
import org.apache.hadoop.hbase.mapreduce.PutSortReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;


/**
 * Bulkload：如果HDFS中有海量数据要导入HBase，可以先将这些数据生成HFile文件，然后
 * 批量导入HBase的数据表中，这样可以极大地提升数据导入HBase的效率。
 * 即利用MapReduce输出HBase内部数据格式的表数据，然后将生成的StoreFiles直接导入到集群中。
 *
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/2/8 16:26.
 * @Modified By:
 */
public class HBaseBulkload
{
    public static boolean bulkloadHBase(String[] args)
        throws Exception
    {
        if (args.length != 4)
        {
            return false;
        }

        String inputPath = args[0];
        String outputPath = args[1];
        String jobName = args[2];
        String tableName = args[3];

        // 配置MapReduce(或者将hadoop的相关配置文件引入项目)
        Configuration hadoopConf = new Configuration();
        hadoopConf.set("fs.defaultFS", "localhost:9000");
        hadoopConf.set("mapred.job.tracker", "localhost:9001");
        Job job = Job.getInstance(hadoopConf, jobName);
        job.setJarByClass(HBaseBulkload.class);
        job.setMapperClass(bulkMapper.class);
        job.setReducerClass(PutSortReducer.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(HFileOutputFormat2.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        // 配置HBase或者将hbase的相关配置文件引入项目
        Configuration hbaseConf = HBaseConfiguration.create();
        hbaseConf.set("hbase.zookeeper.quorum", "localhost");
        hbaseConf.set("hbase.zookeeper.property.clientPort", "2181");

        // 生成HFile
        Connection conn = ConnectionFactory.createConnection(hbaseConf);
        HTable table = (HTable)conn.getTable(TableName.valueOf(tableName));
        HFileOutputFormat2.configureIncrementalLoad(job, table);

        // 执行任务
        job.waitForCompletion(true);

        // 将HFile文件导入HBase
        LoadIncrementalHFiles loader = new LoadIncrementalHFiles(hbaseConf);
        loader.doBulkLoad(new Path(outputPath), table);
        return true;
    }

    public static class bulkMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put>
    {
        @Override
        protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException
        {
            // 将输入数据以tab键分词, "rowKey     value" 为一行
            String[] values = value.toString().split("\t");
            if (values.length == 2)
            {
                // 设置行键、列族、列名和值
                byte[] rowKey = Bytes.toBytes(values[0]);
                byte[] family = Bytes.toBytes("columnFamily");
                byte[] column = Bytes.toBytes("columnName");
                byte[] colValue = Bytes.toBytes(values[1]);
                // 将行键序列化作为mapper输出的key
                ImmutableBytesWritable rowKeyWritable = new ImmutableBytesWritable(rowKey);
                // 将put对象作为mapper输出的value
                Put put = new Put(rowKey);
                put.addColumn(family, column, colValue);
                context.write(rowKeyWritable, put);
            }
        }
    }

}
