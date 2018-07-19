package com.zsm.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.zsm.canal.model.Canals;
import com.zsm.canal.model.Cluster;
import com.zsm.canal.model.Email;
import com.zsm.canal.model.Simple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/3/15.
 * @Modified By:
 */
public class Main
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String WRONG_TIPS = "the something goes wrong when stopping canal:";

    private static final String CANAL_CLIENT_IS_STP = "the canal client is stop:";

    private static final String CANAL_CLIENT_IS_DOWN = "the canal client is down.";

    private static final String CANAL_CLIENT_IS_END = "the canal is killed or ended";

    private static final String START_FLAG = "## ";

    private static final String CONFIG_ERROR = "config error:please check config file! ";

    private static final String ROOT_PATH = System.getProperty("user.dir");

    private static Email EMAIL = null;

    public static void main(String[] args)
    {
        try
        {
            List<CanalClient> clients = createCanalClients();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                for (CanalClient canalClient : clients)
                {
                    String destination = canalClient.getDestination();
                    try
                    {
                        LOGGER.info(START_FLAG + CANAL_CLIENT_IS_STP + destination);
                        canalClient.stop();
                    }
                    catch (Throwable e)
                    {
                        e.printStackTrace();
                        LOGGER.error(WRONG_TIPS, e);
                    }
                    finally
                    {
                        LOGGER.info(START_FLAG + destination + CANAL_CLIENT_IS_DOWN);
                    }
                }
            }));
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            SendMail.sendMails(EMAIL.getFrom(), EMAIL.getPassword(), EMAIL.getSendto(),
                WRONG_TIPS + "main exception:" + ROOT_PATH, e.getMessage());
        }
    }

    private static List<CanalClient> createCanalClients()
    {
        List<CanalClient> clients = new ArrayList<>();
        String kafkaConf = ROOT_PATH + File.separator + "conf" + File.separator + "producer.properties";
        String filePath = ROOT_PATH + File.separator + "conf" + File.separator + "canal-conf.xml";
        Canals canals = analysisXml(filePath);
        EMAIL = canals.getEmails();
        if (!validateConfig(canals))
        {
            throw new RuntimeException(CONFIG_ERROR);
        }

        if ("simple".equals(canals.getPattern()))
        {
            for (Simple simple : canals.getSimples())
            {
                KAFKAProducer producer = new KAFKAProducer(kafkaConf);
                SocketAddress address = new InetSocketAddress(simple.getHostname(), simple.getPort());
                CanalConnector connector = CanalConnectors.newSingleConnector(address, simple.getDestination(),
                    simple.getUsername(), simple.getPassword());

                CanalClient canalClient = new CanalClient(simple.getDestination(), connector, producer,
                    simple.getTopicname());
                canalClient.setEmail(canals.getEmails());
                canalClient.start();
                clients.add(canalClient);
            }
        }

        if ("cluster".equals(canals.getPattern()))
        {
            for (Cluster cluster : canals.getClusters())
            {
                KAFKAProducer producer = new KAFKAProducer(kafkaConf);
                CanalConnector connector = CanalConnectors.newClusterConnector(cluster.getZkservice(),
                    cluster.getDestination(), cluster.getUsername(), cluster.getPassword());

                CanalClient canalClient = new CanalClient(cluster.getDestination(), connector, producer,
                    cluster.getTopicname());
                canalClient.setEmail(canals.getEmails());
                canalClient.start();
                clients.add(canalClient);
            }
        }
        return clients;
    }

    public static boolean validateConfig(Canals canals)
    {
        try
        {
            if ("simple".equals(canals.getPattern()) && canals.getSimples() != null)
            {
                List<Simple> simples = canals.getSimples();
                for (Simple simple : simples)
                {
                    String topicname = simple.getTopicname();
                    boolean exist = KAFKATopic.findTopic(simple.getZkurl(), topicname);
                    if (!exist)
                    {
                        KAFKATopic.createTopic(simple.getZkurl(), topicname, 1, 3);
                    }
                }
            }
            if ("cluster".equals(canals.getPattern()) && canals.getSimples() != null)
            {
                List<Cluster> clusters = canals.getClusters();
                for (Cluster cluster : clusters)
                {
                    String topicname = cluster.getTopicname();
                    String zkService = cluster.getZkservice();
                    String[] zk = zkService.split(",");
                    boolean topic = KAFKATopic.findTopic(zk[0], topicname);
                    if (!topic)
                    {
                        KAFKATopic.createTopic(zk[0], topicname, 1, 3);
                    }
                }
            }
            return true;
        }
        catch (Exception e)
        {
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return false;
    }

    private static Canals analysisXml(String filePath)
    {
        Canals canals = null;
        try
        {
            Unmarshaller unmarshaller = JAXBContext.newInstance(Canals.class).createUnmarshaller();
            canals = (Canals)unmarshaller.unmarshal(new File(filePath));
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
            LOGGER.error("xml analysis fail !", e);
        }
        return canals;
    }
}
