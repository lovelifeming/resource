package com.zsm.bigdata;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;


public class MyProducer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MyProducer.class);

    private String topicName;
   /* private KafkaProducer<String, String> producer;
    
	public MyProducer(String topicName,String producerPropertiesPath) {
		super();
		this.topicName = topicName;
		Properties props = new Properties();
		try {
			props.load(MyProducer.class.getResourceAsStream(producerPropertiesPath));
			this.producer = new KafkaProducer<String, String>(props);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LOGGER.info("未知的配置文件路径");
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info("读取生产者配置文件异常");
		}
	} 
	
	public void send(String data){
		String key = UUID.randomUUID().toString();
		producer.send(new ProducerRecord<String, String>(topicName, key, data));
	}*/

    private Producer<String, String> producer;

    public MyProducer(String topicName, String producerPropertiesPath)
    {
        super();
        this.topicName = topicName;
        Properties props = new Properties();
        try
        {
            //props.load(MyProducer.class.getResourceAsStream(producerPropertiesPath));

//            metadata.broker.list=220.197.198.52:9092
//            acks=1
//            serializer.class=kafka.serializer.StringEncoder
//            key.serializer.class=kafka.serializer.StringEncoder
//            props.put("metadata.broker.list","172.16.152.48:9092");
            props.put("bootstrap.servers", "172.16.152.48:9092");
            props.put("acks","1");
            props.put("serializer.class","kafka.serializer.StringEncoder");
            props.put("key.serializer.class","kafka.serializer.StringEncoder");
            props.put("value.serializer", StringSerializer.class.getName());
            props.put("key.serializer", StringSerializer.class.getName());

            this.producer = new Producer<String, String>(new ProducerConfig(props));
            LOGGER.info("MyProducer "+topicName+" "+props.toString());
        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//            LOGGER.info("未知的配置文件路径 "+e.getMessage());
//            LOGGER.info(producerPropertiesPath);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//            LOGGER.info("读取生产者配置文件异常 "+e.getMessage());
//            LOGGER.info(producerPropertiesPath);
//        }
        catch (Exception e)
        {
            LOGGER.info(producerPropertiesPath);
            e.printStackTrace();
            LOGGER.info("未知的配置文件路径 "+e.getMessage());
        }
    }

    public void send(String data)
        throws Exception
    {
        String key = UUID.randomUUID().toString();
        for (int i = 1; i <= 3; i++)
        {
            if (i > 1)
            {
                System.out.println("数据重发第" + i + "次");
                LOGGER.info("MyProducer send"+i+" "+key);
            }
            try
            {
                producer.send(new KeyedMessage<String, String>(topicName, key, data));
                break;
            }
            catch (Exception e)
            {
                System.out.println("发送消息失败....." +e.getMessage());

                if (i == 3)
                {
                    throw e;
                }
            }
        }
    }
}
