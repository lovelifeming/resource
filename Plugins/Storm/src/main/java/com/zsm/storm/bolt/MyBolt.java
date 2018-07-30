package com.zsm.storm.bolt;

import com.zsm.storm.model.Sample;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.Map;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/7/24.
 * @Modified By:
 */
public class MyBolt implements IRichBolt
{
    OutputCollector collector = null;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector)
    {
        this.collector = outputCollector;
    }

    /**
     * Bolt中的执行方法
     *
     * @param tuple
     */
    @Override
    public void execute(Tuple tuple)
    {
        // 获取从spout传过来的数据，"log"是在spout中declareOutputFields方法中定义输出格式
        try
        {
            if (tuple.getValue(0) != null)
            {
                Sample value = (Sample)tuple.getValue(0);
                System.out.println(value.toString());
            }
            collector.ack(tuple);
        }
        catch (Exception e)
        {
            collector.fail(tuple);
        }
    }

    @Override
    public void cleanup()
    {

    }

    /**
     * 定义输出字段，如果这个流程后面没有下一个bolt就不用输出，有就将下一个流程需要的字段输出
     *
     * @param outputFieldsDeclarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer)
    {
        outputFieldsDeclarer.declare(new Fields(""));
    }

    @Override
    public Map<String, Object> getComponentConfiguration()
    {
        return null;
    }
}
