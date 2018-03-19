package com.zsm.FastDFS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/3/15.
 * @Modified By:
 */
public class Main
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)
        throws Exception
    {
        FastDFSClient fastDFSClient = new FastDFSClient(args[0]);
        String uploadFile = fastDFSClient.uploadFile(args[1]);

        String topic = "BIG_DATA";
        String props = "producer.properties";
        MyProducer producer = new MyProducer(topic, props);
        for (int i = 0; i < 10; i++)
        {
            producer.send(uploadFile);
            LOGGER.info(uploadFile);
        }
        File file = new File("/opt/rh/data/test.jar");
        if (!file.exists())
        {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fastDFSClient.download_file(uploadFile, fos);
//
//        KafkaConsumer kafkaConsumer=new KafkaConsumer(topic);
//        new Thread(kafkaConsumer).start();
    }

}
