package com.zsm.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/24.
 * @Modified By:
 */
public class AbstractCanalClient
{
    protected final static Logger LOGGER = LoggerFactory.getLogger(AbstractCanalClient.class);

    protected static final String SEP = SystemUtils.LINE_SEPARATOR;

    protected static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    protected volatile boolean running = false;

    protected Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler()
    {
        public void uncaughtException(Thread t, Throwable e)
        {
            LOGGER.error(String.format("parse events has an error! Thread name:%s", t.getId() + t.getName()), e);
        }
    };

    protected Thread thread = null;

    protected CanalConnector connector;

    protected static String context_format = null;

    protected static String row_format = null;

    protected static String transaction_format = null;

    protected String destination;

    static
    {
        context_format = SEP + "****************************************************" + SEP;
        context_format += "* Batch Id: [{}] ,count : [{}] , memsize : [{}] , Time : {}" + SEP;
        context_format += "* Start : [{}] " + SEP;
        context_format += "* End : [{}] " + SEP;
        context_format += "****************************************************" + SEP;
        row_format = SEP
                     + "—————-> binlog[{}:{}] , name[{},{}] , eventType : {} , executeTime : {} , delay : {}ms"
                     + SEP;
        transaction_format = SEP + "================> binlog[{}:{}] , executeTime : {} , delay : {}ms" + SEP;
    }

    public AbstractCanalClient(String destination)
    {
        this(destination, null);
    }

    public AbstractCanalClient(String destination, CanalConnector connector)
    {
        this.destination = destination;
        this.connector = connector;
    }

    protected void start()
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
        thread.start();
        running = true;
    }

    protected void stop()
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
                // ignore
            }
        }
        MDC.remove("destination");
    }

    protected void process()
    {
        int batchSize = 5 * 1024;
        while (running)
        {
            try
            {
                MDC.put("destination", destination);
                connector.connect();
                connector.subscribe("gq_p2pget_fy.t_sys_org");
                while (running)
                {
                    Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0)
                    {
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                            LOGGER.info("No,batchId=-1,message.getEntries().size()=0");
                        }
                    }
                    else
                    {
                        System.out.println(message.toString());
                        printSummary(message, batchId, size);
                        printEntry(message.getEntries());
                    }
                    //System.out.println(message.toString());
                    connector.ack(batchId); // 提交确认
                    connector.rollback(batchId); // 处理失败, 回滚数据
                }
            }
            catch (Exception e)
            {
                LOGGER.error("process error!", e);
            }
            finally
            {
                connector.disconnect();
                MDC.remove("destination");
            }
        }
    }

    private void printSummary(Message message, long batchId, int size)
    {
        long memsize = 0;
        for (Entry entry : message.getEntries())
        {
            memsize += entry.getHeader().getEventLength();
        }
        String startPosition = null;
        String endPosition = null;
        if (!CollectionUtils.isEmpty(message.getEntries()))
        {
            startPosition = buildPositionForDump(message.getEntries().get(0));
            endPosition = buildPositionForDump(message.getEntries().get(message.getEntries().size() - 1));
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        LOGGER.info(context_format, new Object[] {batchId, size, memsize, format.format(new Date()), startPosition,
            endPosition});
    }

    protected String buildPositionForDump(Entry entry)
    {
        long time = entry.getHeader().getExecuteTime();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return entry.getHeader().getLogfileName() + ":" + entry.getHeader().getLogfileOffset() + ":"
               + entry.getHeader().getExecuteTime() + "(" + format.format(date) + ")";
    }

    protected void printEntry(List<Entry> entrys)
    {
        for (Entry entry : entrys)
        {
            long executeTime = entry.getHeader().getExecuteTime();
            long delayTime = new Date().getTime() - executeTime;
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN ||
                entry.getEntryType() == EntryType.TRANSACTIONEND)
            {
                if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN)
                {
                    TransactionBegin begin = null;
                    try
                    {
                        begin = TransactionBegin.parseFrom(entry.getStoreValue());
                    }
                    catch (InvalidProtocolBufferException e)
                    {
                        throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                    }
                    // 打印事务头信息，执行的线程id，事务耗时
                    LOGGER.info(transaction_format,
                        new Object[] {entry.getHeader().getLogfileName(),
                            String.valueOf(entry.getHeader().getLogfileOffset()),
                            String.valueOf(entry.getHeader().getExecuteTime()), String.valueOf(delayTime)});
                    LOGGER.info(" BEGIN —-> Thread id: {}", begin.getThreadId());
                }
                else if (entry.getEntryType() == EntryType.TRANSACTIONEND)
                {
                    TransactionEnd end = null;
                    try
                    {
                        end = TransactionEnd.parseFrom(entry.getStoreValue());
                    }
                    catch (InvalidProtocolBufferException e)
                    {
                        throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                    }
                    // 打印事务提交信息，事务id
                    LOGGER.info("—————-\n");
                    LOGGER.info(" END —-> transaction id: {}", end.getTransactionId());
                    LOGGER.info(transaction_format,
                        new Object[] {entry.getHeader().getLogfileName(),
                            String.valueOf(entry.getHeader().getLogfileOffset()),
                            String.valueOf(entry.getHeader().getExecuteTime()), String.valueOf(delayTime)});
                }
                continue;
            }
            if (entry.getEntryType() == EntryType.ROWDATA)
            {
                RowChange rowChage = null;
                try
                {
                    rowChage = RowChange.parseFrom(entry.getStoreValue());
                }
                catch (Exception e)
                {
                    throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                }
                EventType eventType = rowChage.getEventType();
                LOGGER.info(row_format,
                    new Object[] {entry.getHeader().getLogfileName(),
                        String.valueOf(entry.getHeader().getLogfileOffset()), entry.getHeader().getSchemaName(),
                        entry.getHeader().getTableName(), eventType,
                        String.valueOf(entry.getHeader().getExecuteTime()), String.valueOf(delayTime)});
                if (eventType == EventType.QUERY || rowChage.getIsDdl())
                {
                    LOGGER.info(" sql —-> " + rowChage.getSql() + SEP);
                    continue;
                }
                for (RowData rowData : rowChage.getRowDatasList())
                {
                    if (eventType == EventType.DELETE)
                    {
                        printColumn(rowData.getBeforeColumnsList());
                    }
                    else if (eventType == EventType.INSERT)
                    {
                        printColumn(rowData.getAfterColumnsList());
                    }
                    else
                    {
                        printColumn(rowData.getAfterColumnsList());
                    }
                }
            }
        }
    }

    protected void printColumn(List<Column> columns)
    {
        for (Column column : columns)
        {
            StringBuilder builder = new StringBuilder();
            builder.append(column.getName() + " : " + column.getValue());
            builder.append("    type=" + column.getMysqlType());
            if (column.getUpdated())
            {
                builder.append("    update=" + column.getUpdated());
            }
            builder.append(SEP);
            LOGGER.info(builder.toString());
            System.out.println(builder.toString());
        }
    }

    public void setConnector(CanalConnector connector)
    {
        this.connector = connector;
    }
}
