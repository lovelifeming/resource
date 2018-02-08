package com.zsm.bigdata;

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
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/2/1 15:53.
 * @Modified By:
 */
public class MapReduceApp
{
    public static void mapReduce(String[] args)
        throws IOException, ClassNotFoundException, InterruptedException
    {
        //创建Configuration
        Configuration configuration = new Configuration();
        //准备清理环境
        Path outputPath = new Path(args[1]);
        FileSystem fs = FileSystem.get(configuration);
        if (fs.exists(outputPath))
        {
            fs.delete(outputPath, true);
        }

        //创建job,Max temperature是job的名称
        Job job = Job.getInstance(configuration, "maxTemperature");
        //设置job处理类，就是主类
        job.setJarByClass(MapReduceApp.class);
        //处理数据，就必须有一个输入路径，第一个参数job的名称，第二个参数是Path
        FileInputFormat.setInputPaths(job, new Path(args[0]));

        //设置map相关的
        job.setMapperClass(MyMapper.class); //设置MyMapper.class
        job.setOutputKeyClass(Text.class);  //设置map输出key的类型，是Text
        job.setMapOutputValueClass(LongWritable.class); //设置map输出的value的类型

        //设置reduce相关的
        job.setReducerClass(MyReduce.class);    //设置MyReduce.class
        job.setOutputKeyClass(Text.class);      //设置reduce输出key的类型，是Text
        job.setOutputValueClass(LongWritable.class);    //设置reduce输出的value的类型

        //设置任务处理的输出路径
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        boolean result = job.waitForCompletion(true);   //把任务提交
        System.out.println(result ? 0 : 1);
    }

    public static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable>
    {
        @Override
        protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException
        {
            String line = value.toString();
            String[] words = line.split(";");
            for (String word : words)
            {
                if (!word.isEmpty())
                {
                    String[] strings = word.split(",");
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
