package com.zsm.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/28.
 * @Modified By:
 */
public class ClusterCanalClient extends AbstractCanalClient
{

    public ClusterCanalClient(String destination)
    {
        super(destination);
    }

    public static void main(String args[])
    {
        String destination = "example";

        // 基于固定canal server的地址，建立链接，其中一台server发生crash，可以支持failover
        // CanalConnector connector = CanalConnectors.newClusterConnector(
        // Arrays.asList(new InetSocketAddress(
        // AddressUtils.getHostIp(),
        // 11111)),
        // "stability_test", "", "");

        // 基于zookeeper动态获取canal server的地址，建立链接，其中一台server发生crash，可以支持failover
        CanalConnector connector = CanalConnectors.newClusterConnector("127.0.0.1:2181", destination, "", "");

        final ClusterCanalClient clientTest = new ClusterCanalClient(destination);
        clientTest.setConnector(connector);
        clientTest.start();

        Runtime.getRuntime().addShutdownHook(new Thread()
        {

            public void run()
            {
                try
                {
                    LOGGER.info("## stop the canal client");
                    clientTest.stop();
                }
                catch (Throwable e)
                {
                    LOGGER.warn("##something goes wrong when stopping canal:", e);
                }
                finally
                {
                    LOGGER.info("## canal client is down.");
                }
            }

        });
    }
}
