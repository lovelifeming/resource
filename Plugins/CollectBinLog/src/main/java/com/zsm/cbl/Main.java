package com.zsm.cbl;

import com.google.code.or.OpenReplicator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zsm.cbl.model.BinlogMasterStatus;
import com.zsm.cbl.model.MysqlConnection;
import com.zsm.cbl.model.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/3/15.
 * @Modified By:
 */
public class Main
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String host = "192.168.11.131";

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
        //or.setServerId(MysqlConnection.getServerId());
        BinlogMasterStatus bms = MysqlConnection.getBinlogMasterStatus();

        or.setBinlogFileName(bms.getBinlogName());
        //or.setBinlogFileName("mysql-bin.000004");
        or.setBinlogPosition(4);
        or.setBinlogEventListener(new InstanceListener());
        try
        {
            or.start();
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        Thread thread = new Thread(new PrintCDCEvent());
        thread.start();
        timerTask();

    }

    public static void timerTask()
    {
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\tableInfo.log"), true));
                    Set<Map.Entry<TableInfo, String>> entries = InstanceListener.DATABASE_TABLE.entrySet();
                    for (Map.Entry entry : entries)
                    {
                        bw.write(entry.getKey().toString());
                        bw.newLine();
                        bw.flush();
                    }
                    bw.close();
                    InstanceListener.DATABASE_TABLE = new ConcurrentHashMap<>();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    LOGGER.error(e.getMessage());
                }
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, 23, 59, 59);
        Timer timer = new Timer();
        Date date = calendar.getTime();
        timer.schedule(timerTask, date, 60 * 1000);
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
