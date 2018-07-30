package com.zsm.storm;

import com.zsm.storm.bolt.MyBolt;
import com.zsm.storm.spout.MySpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/7/24.
 * @Modified By:
 */
public class RunStorm
{
    public static void run(String[] param)
    {
        /**
         * 第一步：创建Storm拓扑构建器
         */
        TopologyBuilder builder = new TopologyBuilder();

        /**
         * 第二步：设置spout，一共有四个设置方法，可以设置并行数。
         */
        builder.setSpout("spout", new MySpout(), 1);

        /**
         * 第三步：设置bolt
         */
        builder.setBolt("bolt", new MyBolt(), 1).shuffleGrouping("spout");

        /**
         * 第四步：创建配置参数
         */
        Map map = new HashMap();
        map.put(Config.TOPOLOGY_WORKERS, 4);
        /**
         * 第五步：提交拓扑作业
         */
        if (param[0] == "local")
        {
            //本地提交模式
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("mytopology", map, builder.createTopology());
        }
        else
        {
            //集群模式
            try
            {
                StormSubmitter.submitTopology("mytopology", map, builder.createTopology());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
}
