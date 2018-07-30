package com.zsm.storm.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/7/24.
 * @Modified By:
 */
public class MyBoltBase extends BaseRichBolt
{
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector)
    {

    }

    @Override
    public void execute(Tuple tuple)
    {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer)
    {

    }
}
