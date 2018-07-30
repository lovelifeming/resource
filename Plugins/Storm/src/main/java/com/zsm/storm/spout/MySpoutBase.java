package com.zsm.storm.spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;

import java.io.FileReader;
import java.util.Map;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/7/24.
 * @Modified By:
 */
public class MySpoutBase extends BaseRichSpout
{
    private SpoutOutputCollector collector;

    private FileReader fileReader;
    /**
     * open是初始化方法.
     *
     * @param map                  创建Topology时的配置
     * @param topologyContext      所有的Topology数据
     * @param spoutOutputCollector 用来把Spout的数据发射给bolt
     */
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector)
    {

    }

    @Override
    public void nextTuple()
    {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer)
    {

    }
}
