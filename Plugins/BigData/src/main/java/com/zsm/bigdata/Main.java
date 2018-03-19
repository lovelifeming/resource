package com.zsm.bigdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/2/6 16:15.
 * @Modified By:
 */
public class Main
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args)
        throws Exception
    {
//        KafkaConsumer consumer=new KafkaConsumer("Consumer");
//        new Thread(consumer).start();
//
//        KafkaProducer producer=new KafkaProducer("Producer");
//        new Thread(producer).start();
//        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
//        List<String> str=strings.stream().filter(string->!string.isEmpty()).collect(Collectors.toList());

//        Properties props=new Properties();
//        props.put("bootstrap.servers", "172.16.152.48:9092");
//        props.put("zookeeper.connect", "172.16.152.48:2181,172.16.152.48:2182");
//        props.put("group.id", "groupA");
//        props.put("enable.auto.commit", "true");
//        props.put("auto.commit.interval.ms", "1000");
//        props.put("session.timeout.ms", "30000");
//        props.put("auto.offset.reset", "earliest");
//        props.put("key.deserializer", StringDeserializer.class.getName());
//        props.put("value.deserializer", StringDeserializer.class.getName());

//        String topic = "BIG_DATA";
//        String props = "producer.properties";
//        MyProducer producer = new MyProducer(topic, props);
//        for (int i = 0; i < 10; i++)
//        {
//            producer.send("key.deserializer test data" + i);
//        }



    }
}
