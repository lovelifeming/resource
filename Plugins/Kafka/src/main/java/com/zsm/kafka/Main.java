package com.zsm.kafka;

/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/3/15.
 * @Modified By:
 */
public class Main
{
    public static void main(String[] args)
        throws Exception
    {
        String topic = "BIG_DATA";
        String props = "producer.properties";
        MyProducer producer = new MyProducer(topic, props);
        for (int i = 0; i < 10; i++)
        {
            producer.send("key.deserializer test data " + i);
        }
    }
}
