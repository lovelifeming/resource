package com.zsm.canal;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/5/11.
 * @Modified By:
 */
public class KAFKAConsumer
{
    private static final int TIME_OUT = 100;

    /**
     * 实例化kafka消费者
     *
     * @param servers    Kafka集群
     * @param groupId    组名
     * @param keyDeser
     * @param valueDeser
     * @param topics     topic名称
     */
    public void invokedConsumer(String servers, String groupId, String keyDeser, String valueDeser, String... topics)
    {
        Properties props = new Properties();
        // 该地址是Kafka集群的子集，用来探测集群     "192.168.31.130:9092,192.168.31.131:9092,192.168.31.132:9092"
        props.put("bootstrap.servers", servers);
        //指定我们找个消费属于哪个组，consumer的分组id       "test"
        props.put("group.id", groupId);
        //一定要开启kafka的offset自动提交的功能，可以保证我们消费者的数据不丢失
        props.put("enable.auto.commit", "false");
        // 每隔1s，自动提交offsets
        props.put("auto.commit.interval.ms", "1000");
        // Consumer向集群发送自己的心跳，超时则认为Consumer已经死了，kafka会把它的分区分配给其他进程
        props.put("session.timeout.ms", "30000");
        // 反序列化器,与生产者key、value的序列化器一致
        //      "org.apache.kafka.common.serialization.StringDeserializer"
        props.put("key.deserializer", keyDeser);
        //      "org.apache.kafka.common.serialization.StringDeserializer"
        props.put("value.deserializer", valueDeser);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        //指定消费的topic,可以多个       Arrays.asList("test")
        consumer.subscribe(Arrays.asList(topics));

        while (true)
        {
            ConsumerRecords<String, String> records = consumer.poll(TIME_OUT);
            for (ConsumerRecord<String, String> record : records)
            {
                System.out.printf("partition = %d , offset = %d, key = %s, value = %s", record.partition(),
                    record.offset(), record.key(), record.value());
                System.out.println();
            }
        }
    }
}
