package com.zsm.canal;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/5/11.
 * @Modified By:
 */
public class KAFKAPartitioner implements Partitioner
{
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster)
    {
        //key值的hashCode除所有topic取余
        return key.hashCode() % cluster.partitionCountForTopic(topic);
    }

    @Override
    public void close()
    {

    }

    @Override
    public void configure(Map<String, ?> configs)
    {

    }
}
