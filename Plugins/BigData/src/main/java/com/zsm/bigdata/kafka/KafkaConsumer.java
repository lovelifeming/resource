package com.zsm.bigdata.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import scala.actors.threadpool.Arrays;

import java.util.Properties;


/**
 * KAFKA 消费者 消费消息
 *
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/3/7 16:39.
 * @Modified By:
 */
public class KafkaConsumer implements Runnable
{
    private static final String GROUP_ID = "groupA";

    private org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer;

    private ConsumerRecords<String, String> msgList;

    private String topicName;

    public KafkaConsumer(String topicName)
    {
        Properties properties = new Properties();
        //kafka的集群IP地址及端口
        properties.put("bootstrap.servers", "127.0.0.1:9092");
        properties.put("group.id", GROUP_ID);
        //是否自动提交 true为自动提交
        properties.put("enable.auto.commit", "false");
        //自动提交时间间隔
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("session.timeout.ms", "30000");
        properties.put("auto.offset.reset", "earliest");
        //key和value 序列化
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        //zookeeper的集群IP地址及端口
        properties.put("zookeeper.connect", "127.0.0.1:2181,127.0.0.1:2182");
        this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(properties);
        this.topicName = topicName;
        consumer.subscribe(Arrays.asList(new String[] {topicName}));
    }

    @Override
    public void run()
    {
        int messageNum = 0;
        try
        {
            outside:
            while (true)
            {
                msgList = consumer.poll(1000);
                if (msgList != null && msgList.count() > 0)
                {
                    for (ConsumerRecord<String, String> record : msgList)
                    {
                        System.out.println(messageNum + "  receive key:" + record.key() + " receive value:" +
                                           record.value() + "  receive offset:" + record.offset());
                        messageNum++;

                    }
                    //默认消费100条消息
                    if (messageNum > 100)
                    {
                        break outside;
                    }
                }
                else
                {
                    Thread.sleep(1000);
                }
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            consumer.close();
        }

    }
}
