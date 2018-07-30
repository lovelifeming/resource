package com.zsm.canal;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.zsm.canal.model.Email;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.Assert;

import java.util.*;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/5/18.
 * @Modified By:
 */
public class CanalClient
{
    private final static Logger LOGGER = LoggerFactory.getLogger(CanalClient.class);

    private static final String SEP = SystemUtils.LINE_SEPARATOR;

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private KAFKAProducer kafkaProducer;

    private String topicName;

    private CanalConnector connector;

    private String destination;

    private Email email;

    private volatile boolean running = false;

    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler()
    {
        public void uncaughtException(Thread t, Throwable e)
        {
            String message = String.format("parse events has an error! Thread name:%s  destination:%s exception:%s",
                t.getId() + t.getName(), destination, e.getMessage());
            LOGGER.error(message, e);
            if (email != null && email.getSendto().size() > 0)
            {
                for (String to : email.getSendto())
                {
                    SendMail.sendMail(email.getFrom(), email.getPassword(), to, destination, message);
                }
            }
        }
    };

    private Thread thread = null;

    public CanalClient(String destination, CanalConnector connector, KAFKAProducer producer, String topicName)
    {
        this.destination = destination;
        this.connector = connector;
        this.kafkaProducer = producer;
        this.topicName = topicName;
    }

    public void start()
    {
        Assert.notNull(connector, "connector is null");
        thread = new Thread(new Runnable()
        {
            public void run()
            {
                process();
            }
        });
        thread.setUncaughtExceptionHandler(handler);
        thread.setName(destination);
        thread.start();
        running = true;
    }

    public void stop()
    {
        if (!running)
        {
            return;
        }
        running = false;
        if (thread != null)
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
                LOGGER.info("canal is stopping!");
                LOGGER.error(e.getMessage());
            }
        }
        MDC.remove("destination");
    }

    private void process()
    {
        int batchSize = 1024;
        int tries = 0;
        while (running)
        {
            long batchId = -1;
            try
            {
                MDC.put("destination", destination);
                connector.connect();
                //过滤器设置, .*\\..* 是不做任何过滤,不传过滤条件默认采用canal配置文件过滤器
                //connector.subscribe(".*\\..*");
                connector.subscribe();
                while (running)
                {
                    Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                    batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0)
                    {
                        try
                        {
                            Thread.sleep(500);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                            LOGGER.info("No,batchId=-1,message.getEntries().size()=0");
                        }
                    }
                    else
                    {
                        printEntry(message.getEntries());
                    }
                    connector.ack(batchId); // 提交确认
                    //connector.rollback(batchId); // 处理失败, 回滚数据
                }
            }
            catch (Exception e)
            {
                LOGGER.error("process error!", e);
                if (batchId > 0)
                {
                    connector.rollback(batchId);
                }
                tries++;
                if (tries > 9)
                {
                    LOGGER.error("tries connect client,tries " + tries);
                    break;
                }
            }
            finally
            {
                connector.disconnect();
                MDC.remove("destination");
            }
        }
    }

    private void printEntry(List<Entry> entrys)
    {
        for (Entry entry : entrys)
        {
            //System.out.println("*************entrys start, entry type :" + entry.getEntryType().toString());
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN ||
                entry.getEntryType() == EntryType.TRANSACTIONEND)
            {
                continue;
            }
            if (entry.getEntryType() == EntryType.ROWDATA)
            {
                RowChange rowChange = null;
                try
                {
                    rowChange = RowChange.parseFrom(entry.getStoreValue());
                }
                catch (Exception e)
                {
                    throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                }
                EventType eventType = rowChange.getEventType();
                if (eventType == CanalEntry.EventType.QUERY || rowChange.getIsDdl())
                {
                    //LOGGER.info(" sql —-> " + rowChange.getSql() + SEP);
                    continue;
                }
                sendRowData(entry, rowChange, eventType);
            }
            //System.out.println("*************entrys end");
        }
    }

    private void sendRowData(Entry entry, RowChange rowChange, EventType eventType)
    {
        String binLog = String.valueOf(entry.getHeader().getExecuteTime());
        String databaseName = entry.getHeader().getSchemaName();
        String tableName = entry.getHeader().getTableName();
        String type = eventType.toString().toLowerCase();

        List<RowData> list = rowChange.getRowDatasList();
        if (list.size() == 0)
        {
            JSONObject json = combineJSON(databaseName, tableName, type);
            Map<String, String> tm = new HashMap<>();
            tm.put("bt", binLog);
            tm.put("kt", String.valueOf(new Date().getTime()));
            tm.put("sql", rowChange.getSql());
            json.put("data", tm);
            LOGGER.info(json.toString());
            sendToKafka(databaseName, json.toString());
        }

        for (RowData rowData : list)
        {
            JSONObject json = combineJSON(databaseName, tableName, type);
            if (eventType == EventType.DELETE)
            {
                Map<String, String> tm = new HashMap<>();
                tm.put("bt", binLog);
                tm.put("kt", String.valueOf(new Date().getTime()));
                json.put("data", tm);
                json.put("row", combineRow(rowData.getBeforeColumnsList()));
            }
            else if (eventType == EventType.INSERT)
            {
                Map<String, String> data = combineInsertData(rowData.getAfterColumnsList());
                putDataToJSON(binLog, rowData, json, data);
            }
            else
            {
                Map<String, String> data = combineUpdateData(rowData.getAfterColumnsList());
                putDataToJSON(binLog, rowData, json, data);
            }
            String jsonStr = json.toString();
            sendToKafka(databaseName, jsonStr);
            LOGGER.info(jsonStr);
            json = null;
            jsonStr = null;
        }
        binLog = null;
        list = null;
    }

    private static void putDataToJSON(String binLog, CanalEntry.RowData rowData, JSONObject json,
                                      Map<String, String> data)
    {
        json.put("data", data);
        data.put("bt", binLog);
        data.put("kt", String.valueOf(new Date().getTime()));
        json.put("row", combineRow(rowData.getAfterColumnsList()));
    }

    /**
     * 向kafka中传递sql语句
     */
    private void sendToKafka(String databaseName, String phoenixSQL)
    {
        try
        {
            kafkaProducer.send(topicName, databaseName, phoenixSQL);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private static JSONObject combineJSON(String databaseName, String tableName, String type)
    {
        JSONObject json = new JSONObject();
        json.put("database", databaseName);
        json.put("tablename", tableName);
        json.put("type", type);
        return json;
    }

    private static String combineRow(List<CanalEntry.Column> columns)
    {
        StringBuilder sb = new StringBuilder();
        for (CanalEntry.Column column : columns)
        {
            if (column.getIsKey())
            {
                sb.append(column.getValue() + "_");
            }
        }
        String row = sb.toString();
        if (row.endsWith("_"))
        {
            row = row.substring(0, row.lastIndexOf("_"));
        }
        return row;
    }

    private static Map<String, String> combineInsertData(List<CanalEntry.Column> columns)
    {
        Map<String, String> map = new LinkedHashMap<>();
        for (CanalEntry.Column column : columns)
        {
            map.put(column.getName(), column.getValue());
        }
        return map;
    }

    private static Map<String, String> combineUpdateData(List<CanalEntry.Column> columns)
    {
        Map<String, String> map = new LinkedHashMap<>();
        for (CanalEntry.Column column : columns)
        {
            if (column.getUpdated())
            {
                map.put(column.getName(), column.getValue());
            }
        }
        return map;
    }

    public String getDestination()
    {
        return destination;
    }

    public void setEmail(Email email)
    {
        this.email = email;
    }
}
