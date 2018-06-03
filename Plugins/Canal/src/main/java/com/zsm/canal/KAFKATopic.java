package com.zsm.canal;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/5/21.
 * @Modified By:
 */
public class KAFKATopic
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * 会话超时时间
     */
    private static final int SESSION_TIME_OUT = 30000;

    /**
     * 连接超时时间
     */
    private static final int CONNECTION_TIME_OUT = 30000;

    /**
     * 创建topic
     *
     * @param zkUrl       zk服务器IP地址
     * @param topicName   topic名字
     * @param partitions  分区数
     * @param replication 复制备份数
     */
    public static boolean createTopic(String zkUrl, String topicName, int partitions, int replication)
    {
        try
        {
            ZkUtils zkUtils = ZkUtils.apply(zkUrl, SESSION_TIME_OUT, CONNECTION_TIME_OUT,
                JaasUtils.isZkSecurityEnabled());

            AdminUtils.createTopic(zkUtils, topicName, partitions, replication, new Properties(),
                RackAwareMode.Enforced$.MODULE$);
            zkUtils.close();
            return true;
        }
        catch (Exception e)
        {
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除topic
     *
     * @param zkUrl     zk服务器IP地址
     * @param topicName topic名字
     */
    public static boolean deleteTopic(String zkUrl, String topicName)
    {
        try
        {
            ZkUtils zkUtils = ZkUtils.apply(zkUrl, SESSION_TIME_OUT, CONNECTION_TIME_OUT,
                JaasUtils.isZkSecurityEnabled());
            AdminUtils.deleteTopic(zkUtils, topicName);
            zkUtils.close();
            return true;
        }
        catch (Exception e)
        {
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询topic
     *
     * @param zkUrl     zk服务器IP地址
     * @param topicName topic名字
     * @return boolean  true:topic存在，false：topic不存在
     */
    public static boolean findTopic(String zkUrl, String topicName)
    {
        ZkUtils zkUtils = ZkUtils.apply(zkUrl, SESSION_TIME_OUT, CONNECTION_TIME_OUT,
            JaasUtils.isZkSecurityEnabled());
        return AdminUtils.topicExists(zkUtils, topicName);
    }

    /**
     * 查询Topic属性信息
     *
     * @param zkUrl     zk服务器IP地址
     * @param topicName topic名字
     */
    public static Map<Object, Object> findTopicConfig(String zkUrl, String topicName)
    {
        Map<Object, Object> conf = new HashMap<>();
        try
        {
            ZkUtils zkUtils = ZkUtils.apply(zkUrl, SESSION_TIME_OUT, CONNECTION_TIME_OUT,
                JaasUtils.isZkSecurityEnabled());
            //获取topic的属性信息
            Properties prop = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topicName);
            Set<Map.Entry<Object, Object>> entries = prop.entrySet();
            Iterator it = entries.iterator();
            while (it.hasNext())
            {
                Map.Entry entry = (Map.Entry)it.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                conf.put(key, value);
            }
            zkUtils.close();
        }
        catch (Exception e)
        {
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return conf;
    }

    /**
     * 修改Topic 属性信息
     *
     * @param zkUrl       zk服务器IP地址
     * @param topicName   topic名字
     * @param addProperty 添加属性
     * @param delProperty 删除属性
     * @return
     */
    public static boolean modifyTopic(String zkUrl, String topicName, Map<String, String> addProperty,
                                      Map<String, String> delProperty)
    {
        try
        {
            ZkUtils zkUtils = ZkUtils.apply(zkUrl, SESSION_TIME_OUT, SESSION_TIME_OUT, JaasUtils.isZkSecurityEnabled());
            Properties prop = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topicName);
            for (Map.Entry entry : addProperty.entrySet())
            {
                prop.put(entry.getKey(), entry.getValue());
            }
            for (Map.Entry entry : delProperty.entrySet())
            {
                prop.put(entry.getKey(), entry.getValue());
            }
            AdminUtils.changeTopicConfig(zkUtils, topicName, prop);
            zkUtils.close();
            return true;
        }
        catch (Exception e)
        {
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return false;
    }

}
