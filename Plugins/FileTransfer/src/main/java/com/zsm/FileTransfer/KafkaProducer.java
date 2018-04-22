package com.zsm.FileTransfer;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.UUID;


/**
 * KAFKA 生产者 生产消息     topic name必须与在Kafka上面手动创建的topic同名
 */
public class KafkaProducer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private String topicName;

    private Producer<String, String> producer;

    public KafkaProducer(String topicName, String producerPropertiesPath)
    {
        this.topicName = topicName;
        Properties props = new Properties();
        try
        {
            //props.load(KafkaProducer.class.getResourceAsStream(producerPropertiesPath));s
            props.put("metadata.broker.list", "127.0.0.1:9092");
            props.put("bootstrap.servers", "127.0.0.1:9092");
            props.put("acks", "1");
            props.put("group.id", "groupA");
            props.put("serializer.class", "kafka.serializer.StringEncoder");
            props.put("key.serializer.class", "kafka.serializer.StringEncoder");
            props.put("value.serializer", StringSerializer.class.getName());
            props.put("key.serializer", StringSerializer.class.getName());

            this.producer = new Producer<String, String>(new ProducerConfig(props));
            LOGGER.info("KafkaProducer " + topicName + " " + props.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.info(producerPropertiesPath + " 配置文件异常 " + e.getMessage());
        }
    }

    public void send(String data)
        throws Exception
    {
        String key = UUID.randomUUID().toString();
        //发送消息，如果第一次发送成功就退出，如果失败就循环尝试发送三次，三次失败就抛出异常
        for (int i = 1; i <= 3; i++)
        {
            if (i > 1)
            {
                System.out.println("数据第" + i + "次重发");
                LOGGER.info("KafkaProducer send" + i + " " + key);
            }
            try
            {
                producer.send(new KeyedMessage<String, String>(topicName, key, data));
                break;
            }
            catch (Exception e)
            {
                System.out.println("发送消息失败....." + e.getMessage());
                if (i == 3)
                {
                    throw e;
                }
            }
        }
    }
}
