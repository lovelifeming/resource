package com.zsm.storm.spout;

import com.zsm.storm.model.Sample;
import org.apache.poi.ss.usermodel.*;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 生成数据流
 *
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/7/24.
 * @Modified By:
 */
public class MySpout implements IRichSpout
{
    private SpoutOutputCollector collector;

    private List<Sample> samples;

    private int indexInt = 0;

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
        //初始化发射器
        this.collector = spoutOutputCollector;
        this.samples = readExcel("D:\\\\文本分析算法\\\\原始数据.xlsx");
    }

    @Override
    public void close()
    {

    }

    @Override
    public void activate()
    {

    }

    @Override
    public void deactivate()
    {

    }

    /**
     * 这是Spout最主要的方法，在这里我们读取文本文件，并把它的每一行发射出去（给bolt）
     * 这个方法会不断被调用，为了降低它对CPU的消耗，当任务完成时让它sleep一下
     */
    @Override
    public void nextTuple()
    {
//        try
//        {
//            this.collector.emit(new Values("tetsdd!!!!!!AAAAA"), "test");

//            while ((temp = reader.readLine()) != null)
//            {
//                this.collector.emit(new Values(temp), temp);
//            }
        if (samples.size() <= 0)
        {
            indexInt = -1;
        }
        if (samples.size() > 0 && indexInt < samples.size() && indexInt >= 0)
        {
            this.collector.emit(new Values(samples.get(indexInt)), "test");
            indexInt++;
        }
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
    }

    @Override
    public void ack(Object o)
    {

    }

    @Override
    public void fail(Object o)
    {

    }

    /**
     * 定义数据发送的格式
     *
     * @param outputFieldsDeclarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer)
    {
        outputFieldsDeclarer.declare(new Fields("log"));
        System.out.println(outputFieldsDeclarer.toString());
    }

    @Override
    public Map<String, Object> getComponentConfiguration()
    {
        return null;
    }

    private List<Sample> readExcel(String filePath)
    {
        List<Sample> samples = new ArrayList<>();
        try
        {
            InputStream is = new FileInputStream(new File(filePath)); //"D:\\文本分析算法\\原始数据.xlsx"));
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet)
            {
                String[] values = new String[row.getLastCellNum()];
                int index = 0;
                for (Cell cell : row)
                {
                    String value = new DataFormatter().formatCellValue(cell);
                    values[index] = value;
                    index++;
                }
                Sample sample = new Sample();
                sample.setId(values[0]);
                if (values.length > 1)
                {
                    sample.setResult(values[1]);
                }
                if (values.length > 2)
                {
                    sample.setCompare(values[2]);
                }
                samples.add(sample);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return samples;
    }
}
