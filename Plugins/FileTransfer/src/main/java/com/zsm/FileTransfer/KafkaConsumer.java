package com.zsm.FileTransfer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

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
        properties.put("zookeeper.connect", "127.0.0.1:2181,127.0.0.1:2182");

        this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(topicName));
        this.topicName = topicName;
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
                        String temp = messageNum + "  receive key:" + record.key() + " receive value:" +
                                      record.value() + "  receive offset:" + record.offset();
                        System.out.println(temp);
                        messageNum++;
                        LOGGER.info(temp);
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
            LOGGER.info(e.getMessage());
        }
        finally
        {
            consumer.close();
        }
    }
}
