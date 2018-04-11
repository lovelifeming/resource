package com.zsm.cbl;

import com.google.code.or.OpenReplicator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zsm.cbl.model.BinlogMasterStatus;
import com.zsm.cbl.model.MysqlConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/3/15.
 * @Modified By:
 */
public class Main
{
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final String host = "192.168.11.184";

    private static final int port = 3306;

    private static final String user = "root";

    private static final String password = "123456";

    public static void main(String[] args)
        throws Exception
    {
        OpenReplicator or = new OpenReplicator();
        or.setUser(user);
        or.setPassword(password);
        or.setHost(host);
        or.setPort(port);

        MysqlConnection.setConnection(host, port, user, password);
        //配置里的serverId是open-replicator(作为一个slave)的id,不是master的serverId
//      or.setServerId(MysqlConnection.getServerId());

        BinlogMasterStatus bms = MysqlConnection.getBinlogMasterStatus();

        or.setBinlogFileName(bms.getBinlogName());
//      or.setBinlogFileName("mysql-bin.000004");
        or.setBinlogPosition(389);
        or.setBinlogEventListener(new InstanceListener());
        try
        {
            or.start();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        Thread thread = new Thread(new PrintCDCEvent());
        thread.start();
    }

    public static class PrintCDCEvent implements Runnable
    {
        @Override
        public void run()
        {
            while (true)
            {
                if (CDCEventManager.queue.isEmpty() == false)
                {
                    CDCEvent ce = CDCEventManager.queue.pollFirst();
                    Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                    String prettyStr1 = gson.toJson(ce);
                    System.out.println(prettyStr1);
                }
                else
                {
                    try
                    {
                        TimeUnit.SECONDS.sleep(1);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
