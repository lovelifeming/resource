package com.zsm.canal;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/5/4.
 * @Modified By:
 */
public class KAFKAProducer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KAFKAProducer.class);

    private KafkaProducer<String, String> producer;

    public KAFKAProducer(String producerPropertiesPath)
    {
        Properties props = new Properties();
        try
        {
            props.load(new InputStreamReader(new FileInputStream(producerPropertiesPath)));
            //配置在resources目录下面
            //props.load(KafkaProducer.class.getResourceAsStream(producerPropertiesPath));
            this.producer = new KafkaProducer<String, String>(props);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            LOGGER.error("File not found", e);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.error("IO Exception", e);
        }
    }

    public void send(String topicName, String key, String data)
        throws Exception
    {
        producer.send(new ProducerRecord<String, String>(topicName, key, data));
    }

    /**
     * 创建一个生产者发送消息
     *
     * @param servers   Kafka集群地址
     * @param retries   重试的次数
     * @param keySeri   序列化的方式
     * @param valueSeri 序列化的方式
     * @param partClass 自定义分区类
     * @param topicName topic名称
     * @param key       发送消息key值
     * @param value     发送消息value值
     */
    public static void invokedProducer(String servers, int retries, String keySeri, String valueSeri,
                                       String partClass, String topicName, String key, String value)
    {
        Properties props = new Properties();
        // 该地址是集群的子集，用来探测集群     "192.168.31.130:9092,192.168.31.131:9092,192.168.31.132:9092"
        props.put("bootstrap.servers", servers);
        // 记录完整提交，最慢的但是最大可能的持久化
        props.put("acks", "all");
        // 发送失败重试的次数 3
        props.put("retries", retries);
        props.put("reconnect.backoff.ms ", 20000);
        props.put("retry.backoff.ms", 20000);
        // batch的大小
        props.put("batch.size", 16384);
        // 默认情况即使缓冲区有剩余的空间，也会立即发送请求，设置一段时间用来等待从而将缓冲区填的更多，单位为毫秒，
        // producer发送数据会延迟1ms，可以减少发送到kafka服务器的请求数据
        props.put("linger.ms", 1);
        // 提供给生产者缓冲内存总量
        props.put("buffer.memory", 33554432);
        // 序列化的方式       "org.apache.kafka.common.serialization.StringSerializer"
        props.put("key.serializer", keySeri);
        //                  "org.apache.kafka.common.serialization.StringSerializer"
        props.put("value.serializer", valueSeri);
        if (!StringUtils.isEmpty(partClass))
        {
            // 设置属性自定义分区类
            props.put("partitioner.class", "com.east.spark.kafka.MyPartition");
        }
        KafkaProducer<String, String> producer = new KafkaProducer(props);
        for (int i = 0; i < 10; i++)
        {
            // 三个参数分别为topic, key,value，send()是异步发送的，添加到缓冲区立即返回，更高效。
            producer.send(new ProducerRecord<String, String>(topicName, key, value));
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                LOGGER.error("Interrupted Exception", e);
            }
        }
        producer.close();
    }
}
