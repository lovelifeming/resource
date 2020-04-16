package com.zsm.kafka;

import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DemoApplication
{

    public static void main(String[] args)
    {
        //SpringApplication.run(DemoApplication.class, args);
        KafkaConsumer consumer=new KafkaConsumer("device-real-data");
        consumer.run();
    }

}
