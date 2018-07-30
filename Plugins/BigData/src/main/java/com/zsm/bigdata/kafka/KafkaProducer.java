package com.zsm.bigdata.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;


/**
 * KAFKA生产者生产消息
 *
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/3/7 16:16.
 * @Modified By:
 */
public class KafkaProducer implements Runnable
{
    private org.apache.kafka.clients.producer.KafkaProducer<String, String> producer;

    private String topicName;

    public KafkaProducer(String topicName)
    {
        Properties properties = new Properties();
        //配置kafka集群的broker地址，建议配置两个以上，以免其中一个失效，但不需要配全，集群会自动查找leader节点。
        properties.put("metadata.broker.list", "localhost:9092");
        //bootstrap.servers： kafka服务器地址 127.0.0.1:2181
        properties.put("bootstrap.servers", "127.0.0.1:9092");
        // acks:消息的确认机制，默认值是0
        // 0是生产者不会等待kafka的响应
        // 1是kafka会把这条消息写到本地日志文件中，但是不会等待集群中其他机器的成功响应
        // all是leader会等待所有的follower同步完成，确保消息不会丢失，除非kafka集群中所有机器挂掉。这是最强的可用性保证。
        properties.put("acks", "all");
        properties.put("group.id", "groupA");
        // retries：配置为大于0的值的话，客户端会在消息发送失败时重新发送
        properties.put("retries", 3);
        properties.put("reconnect.backoff.ms ", 20000);
        properties.put("retry.backoff.ms", 20000);
        //batch.size:当多条消息需要发送到同一个分区时，生产者会尝试合并网络请求以提高client和生产者的效率。
        properties.put("batch.size", 16384);
        //key.serializer: 键序列化，默认org.apache.kafka.common.serialization.StringDeserializer
        properties.put("key.serializer", StringSerializer.class.getName());
        //value.deserializer:值序列化，默认org.apache.kafka.common.serialization.StringDeserializer
        properties.put("value.serializer", StringSerializer.class.getName());
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("key.serializer.class", "kafka.serializer.StringEncoder");
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(properties);
        this.topicName = topicName;
    }

    @Override
    public void run()
    {
        int messageCount = 0;
        try
        {
            outside:
            while (true)
            {
                //生产数据
                String messageStr = "this is " + messageCount + " message information";
                //发送消息Topic名称，消息键名，消息内容
                producer.send(new ProducerRecord<String, String>(topicName, "message", messageStr));
                messageCount++;
                //累计生产100条数据
                if (messageCount > 100)
                {
                    break outside;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            producer.close();
        }
    }
}
