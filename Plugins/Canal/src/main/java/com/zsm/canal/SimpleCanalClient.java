package com.zsm.canal;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/24.
 * @Modified By:
 */

public class SimpleCanalClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCanalClient.class);

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void startUp(String args[])
    {
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(AddressUtils.getHostIp(),
            11111), "example", "root", "123456");
        int batchSize = 1000;
        int emptyCount = 0;
        try
        {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            int totalEmptyCount = 120;
            while (emptyCount < totalEmptyCount)
            {
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(batchSize);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0)
                {
                    emptyCount++;
                    System.out.println("empty count : " + emptyCount);
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
                else
                {
                    emptyCount = 0;
                    // System.out.printf("message[batchId=%s,size=%s] \n", batchId, size);
                    printEntry(message.getEntries());
                }
                connector.ack(batchId); // 提交确认
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }
            System.out.println("empty too many times, exit");
        }
        finally
        {
            connector.disconnect();
        }
    }

    private static void printEntry(List<Entry> entrys)
    {
        for (Entry entry : entrys)
        {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND)
            {
                continue;
            }
            RowChange rowChange = null;
            try
            {
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            }
            catch (Exception e)
            {
                throw new RuntimeException("ERROR ## parser of manage-event has an error, data:" + entry.toString(), e);
            }

            EventType eventType = rowChange.getEventType();
            String databaseName = entry.getHeader().getSchemaName();
            String tableName = entry.getHeader().getTableName();
//            System.out.println(String.format("================&gt; binlog[%s:%s] , name[%s,%s] , eventType : %s",
//                entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
//                databaseName, tableName, eventType));

            JSONObject json = new JSONObject();
            json.put("database", databaseName);
            json.put("tablename", tableName);
            json.put("type", eventType);
            for (RowData rowData : rowChange.getRowDatasList())
            {

                if (eventType == EventType.DELETE)
                {
                    json.put("data", printColumn(rowData.getBeforeColumnsList()));
                }
                else if (eventType == EventType.INSERT)
                {
                    json.put("data", printColumn(rowData.getAfterColumnsList()));
                }
                else
                {
                    System.out.println("-------&gt; before");
                    json.put("data", printColumn(rowData.getBeforeColumnsList()));
                    System.out.println("-------&gt; after");
                    json.put("data", printColumn(rowData.getAfterColumnsList()));
                }
            }
        }
    }

    private static String printColumn(List<Column> columns)
    {
        //for (Column column : columns)
        //{
        //    System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        //}

        StringBuilder builder = new StringBuilder();
        for (Column column : columns)
        {
            builder.append(column.getName() + " = " + column.getValue());
            builder.append("    type=" + column.getMysqlType());
            if (column.getUpdated())
            {
                builder.append("    update=" + column.getUpdated());
            }
            builder.append(LINE_SEPARATOR);
        }
        return builder.toString();
    }
}
