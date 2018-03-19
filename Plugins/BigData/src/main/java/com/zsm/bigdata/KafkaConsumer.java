package com.zsm.bigdata;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;
import java.util.regex.Pattern;


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
        properties.put("bootstrap.servers", "127.0.0.1:9092");
        properties.put("group.id", GROUP_ID);
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("session.timeout.ms", "30000");
        properties.put("auto.offset.reset", "earliest");
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(properties);
//        consumer.subscribe(Pattern.compile(topicName));
        this.topicName = topicName;
    }

    @Override
    public void run()
    {
        int messageNum = 1;
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
