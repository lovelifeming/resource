package com.zsm.bigdata.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;


/**
 * MapReduce 基本操作实例
 *
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/2/1 15:53.
 * @Modified By:
 */
public class MapReduceOperator
{
    /**
     * MapReduce 基本操作
     * @param args  输入路径，输出路径 job名称
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public static boolean mapReduce(String[] args)
        throws IOException, ClassNotFoundException, InterruptedException
    {
        if (args.length != 3)
        {
            return false;
        }
        String inputPath = args[0];
        String outPath = args[1];
        String jobName = args[2];
        //创建Configuration
        Configuration configuration = new Configuration();
        //准备清理环境
        Path outputPath = new Path(outPath);
        FileSystem fs = FileSystem.get(configuration);
        if (fs.exists(outputPath))
        {
            fs.delete(outputPath, true);
        }

        //创建job,Max temperature是job的名称
        Job job = Job.getInstance(configuration, jobName);
        //设置job处理类，就是主类
        job.setJarByClass(MapReduceOperator.class);
        //处理数据，就必须有一个输入路径，第一个参数job的名称，第二个参数是Path
        FileInputFormat.setInputPaths(job, new Path(inputPath));

        //设置map相关的
        job.setMapperClass(MyMapper.class); //设置MyMapper.class
        job.setOutputKeyClass(Text.class);  //设置map输出key的类型，是Text
        job.setMapOutputValueClass(LongWritable.class); //设置map输出的value的类型

        //设置reduce相关的
        job.setReducerClass(MyReduce.class);    //设置MyReduce.class
        job.setOutputKeyClass(Text.class);      //设置reduce输出key的类型，是Text
        job.setOutputValueClass(LongWritable.class);    //设置reduce输出的value的类型

        //设置任务处理的输出路径
        FileOutputFormat.setOutputPath(job, new Path(outPath));
        //执行MR任务
        boolean result = job.waitForCompletion(true);
        return result;
    }

    public static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable>
    {
        //1,23,25,28,249,248,2017-01-01 00:00:00;
        @Override
        protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException
        {
            String line = value.toString();
            String[] elements = line.split(";");
            for (String element : elements)
            {
                if (!element.isEmpty())
                {
                    String[] strings = element.split(",");
                    context.write(new Text(strings[0]), new LongWritable(Integer.valueOf(strings[2])));
                }
            }
        }
    }


    public static class MyReduce extends Reducer<Text, LongWritable, Text, LongWritable>
    {
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context)
            throws IOException, InterruptedException
        {
            int maxVlaue = Integer.MIN_VALUE;
            for (LongWritable value : values)
            {
                maxVlaue = (int)Math.max(maxVlaue, value.get());
            }
            context.write(key, new LongWritable(maxVlaue));
        }
    }

}
