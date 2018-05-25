package com.zsm.cbl;

import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;
import com.google.code.or.binlog.impl.event.*;
import com.google.code.or.common.glossary.Column;
import com.google.code.or.common.glossary.Pair;
import com.google.code.or.common.glossary.Row;
import com.google.code.or.common.util.MySQLConstants;
import com.zsm.cbl.model.ColumnInfo;
import com.zsm.cbl.model.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/9.
 * @Modified By:
 */
public class InstanceListener implements BinlogEventListener
{
    private static final Logger logger = LoggerFactory.getLogger(InstanceListener.class);

    private static final WriteBinLog WRITE_BIN_LOG = new WriteBinLog();

    public static volatile Map<TableInfo, String> DATABASE_TABLE = new ConcurrentHashMap<>();

    @Override
    public void onEvents(BinlogEventV4 be)
    {
        if (be == null)
        {
            logger.error("binlog event is null");
            return;
        }
        int eventType = be.getHeader().getEventType();
        switch (eventType)
        {
            //格式描述事件
            case MySQLConstants.FORMAT_DESCRIPTION_EVENT:
            {
                logger.trace("FORMAT_DESCRIPTION_EVENT");
                break;
            }
            //每次ROW_EVENT前都伴随一个TABLE_MAP_EVENT事件，保存一些表信息，如tableId, tableName, databaseName, 而ROW_EVENT只有tableId
            case MySQLConstants.TABLE_MAP_EVENT:
            {
                TableMapEvent tme = (TableMapEvent)be;
                TableInfoKeeper.saveTableIdMap(tme);
                logger.trace("TABLE_MAP_EVENT:tableId:{}", tme.getTableId());
                break;
            }
            //删除数据事件，对应delete操作
            case MySQLConstants.DELETE_ROWS_EVENT:
            {
                DeleteRowsEvent dre = (DeleteRowsEvent)be;
                long tableId = dre.getTableId();
                logger.trace("DELETE_ROW_EVENT:tableId:{}", tableId);

                TableInfo tableInfo = TableInfoKeeper.getTableInfo(tableId);
                String databaseName = tableInfo.getDatabaseName();
                String tableName = tableInfo.getTableName();

                List<Row> rows = dre.getRows();
                for (Row row : rows)
                {
                    List<Column> before = row.getColumns();
                    Map<String, String> beforeMap = getMap(before, databaseName, tableName);
                    if (beforeMap != null && beforeMap.size() > 0)
                    {
                        CDCEvent cdcEvent = new CDCEvent(dre, databaseName, tableName);
                        cdcEvent.setDdl(false);
                        cdcEvent.setSql(null);
                        cdcEvent.setBefore(beforeMap);
                        CDCEventManager.queue.addLast(cdcEvent);
                        logger.info("cdcEvent:{}", cdcEvent);
                    }
                }
                DATABASE_TABLE.put(tableInfo, "delete");
                break;
            }
            case MySQLConstants.DELETE_ROWS_EVENT_V2:
            {
                DeleteRowsEventV2 dre = (DeleteRowsEventV2)be;
                long tableId = dre.getTableId();
                logger.trace("DELETE_ROW_EVENT:tableId:{}", tableId);

                TableInfo tableInfo = TableInfoKeeper.getTableInfo(tableId);
                String databaseName = tableInfo.getDatabaseName();
                String tableName = tableInfo.getTableName();

                List<Row> rows = dre.getRows();
                for (Row row : rows)
                {
                    List<Column> before = row.getColumns();
                    Map<String, String> beforeMap = getMap(before, databaseName, tableName);
                    if (beforeMap != null && beforeMap.size() > 0)
                    {
                        CDCEvent cdcEvent = new CDCEvent(dre, databaseName, tableName);
                        cdcEvent.setDdl(false);
                        cdcEvent.setSql(null);
                        cdcEvent.setBefore(beforeMap);
                        CDCEventManager.queue.addLast(cdcEvent);
                        logger.info("cdcEvent:{}", cdcEvent);
                    }
                }
                DATABASE_TABLE.put(tableInfo, "delete");
                break;
            }
            //更新数据事件，对应update操作
            case MySQLConstants.UPDATE_ROWS_EVENT:
            {
                UpdateRowsEvent upe = (UpdateRowsEvent)be;
                long tableId = upe.getTableId();
                logger.info("UPDATE_ROWS_EVENT:tableId:{}", tableId);

                TableInfo tableInfo = TableInfoKeeper.getTableInfo(tableId);
                String databaseName = tableInfo.getDatabaseName();
                String tableName = tableInfo.getTableName();

                List<Pair<Row>> rows = upe.getRows();
                for (Pair<Row> p : rows)
                {
                    List<Column> colsBefore = p.getBefore().getColumns();
                    List<Column> colsAfter = p.getAfter().getColumns();

                    Map<String, String> beforeMap = getMap(colsBefore, databaseName, tableName);
                    Map<String, String> afterMap = getMap(colsAfter, databaseName, tableName);
                    if (beforeMap != null && afterMap != null && beforeMap.size() > 0 && afterMap.size() > 0)
                    {
                        CDCEvent cdcEvent = new CDCEvent(upe, databaseName, tableName);
                        cdcEvent.setDdl(false);
                        cdcEvent.setSql(null);
                        cdcEvent.setBefore(beforeMap);
                        cdcEvent.setAfter(afterMap);
                        CDCEventManager.queue.addLast(cdcEvent);
                        logger.info("cdcEvent:{}", cdcEvent);
                    }
                }
                DATABASE_TABLE.put(tableInfo, "update");
                break;
            }
            case MySQLConstants.UPDATE_ROWS_EVENT_V2:
            {
                UpdateRowsEventV2 upe = (UpdateRowsEventV2)be;
                long tableId = upe.getTableId();
                logger.info("UPDATE_ROWS_EVENT:tableId:{}", tableId);

                TableInfo tableInfo = TableInfoKeeper.getTableInfo(tableId);
                String databaseName = tableInfo.getDatabaseName();
                String tableName = tableInfo.getTableName();

                List<Pair<Row>> rows = upe.getRows();
                for (Pair<Row> p : rows)
                {
                    List<Column> colsBefore = p.getBefore().getColumns();
                    List<Column> colsAfter = p.getAfter().getColumns();

                    Map<String, String> beforeMap = getMap(colsBefore, databaseName, tableName);
                    Map<String, String> afterMap = getMap(colsAfter, databaseName, tableName);
                    if (beforeMap != null && afterMap != null && beforeMap.size() > 0 && afterMap.size() > 0)
                    {
                        CDCEvent cdcEvent = new CDCEvent(upe, databaseName, tableName);
                        cdcEvent.setDdl(false);
                        cdcEvent.setSql(null);
                        cdcEvent.setBefore(beforeMap);
                        cdcEvent.setAfter(afterMap);
                        CDCEventManager.queue.addLast(cdcEvent);
                        logger.info("cdcEvent:{}", cdcEvent);
                    }
                }
                DATABASE_TABLE.put(tableInfo, "update");
                break;
            }
            //插入数据事件，对应insert操作
            case MySQLConstants.WRITE_ROWS_EVENT:
            {
                WriteRowsEvent wre = (WriteRowsEvent)be;
                long tableId = wre.getTableId();
                logger.trace("WRITE_ROWS_EVENT:tableId:{}", tableId);

                TableInfo tableInfo = TableInfoKeeper.getTableInfo(tableId);
                String databaseName = tableInfo.getDatabaseName();
                String tableName = tableInfo.getTableName();

                List<Row> rows = wre.getRows();
                for (Row row : rows)
                {
                    List<Column> after = row.getColumns();
                    Map<String, String> afterMap = getMap(after, databaseName, tableName);
                    if (afterMap != null && afterMap.size() > 0)
                    {
                        CDCEvent cdcEvent = new CDCEvent(wre, databaseName, tableName);
                        cdcEvent.setDdl(false);
                        cdcEvent.setSql(null);
                        cdcEvent.setAfter(afterMap);
                        CDCEventManager.queue.addLast(cdcEvent);
                        logger.info("cdcEvent:{}", cdcEvent);
                    }
                }
                DATABASE_TABLE.put(tableInfo, "insert");
                break;
            }
            case MySQLConstants.WRITE_ROWS_EVENT_V2:
            {
                WriteRowsEventV2 wre = (WriteRowsEventV2)be;
                long tableId = wre.getTableId();
                logger.trace("WRITE_ROWS_EVENT:tableId:{}", tableId);

                TableInfo tableInfo = TableInfoKeeper.getTableInfo(tableId);
                String databaseName = tableInfo.getDatabaseName();
                String tableName = tableInfo.getTableName();

                List<Row> rows = wre.getRows();
                for (Row row : rows)
                {
                    List<Column> after = row.getColumns();
                    Map<String, String> afterMap = getMap(after, databaseName, tableName);
                    if (afterMap != null && afterMap.size() > 0)
                    {
                        CDCEvent cdcEvent = new CDCEvent(wre, databaseName, tableName);
                        cdcEvent.setDdl(false);
                        cdcEvent.setSql(null);
                        cdcEvent.setAfter(afterMap);
                        CDCEventManager.queue.addLast(cdcEvent);
                        logger.info("cdcEvent:{}", cdcEvent);
                    }
                }
                DATABASE_TABLE.put(tableInfo, "insert");
                break;
            }
            //QUERY_EVENT类型的事件通常在以下几种情况下使用:
            // 1. 事务开始时，执行的BEGIN操作。 2. STATEMENT格式中的DML操作。  3. ROW格式中的DDL操作。
            case MySQLConstants.QUERY_EVENT:
            {
                QueryEvent qe = (QueryEvent)be;
                TableInfo tableInfo = createTableInfo(qe);
                if (tableInfo == null)
                    break;
                String databaseName = tableInfo.getDatabaseName();
                String tableName = tableInfo.getTableName();
                logger.trace("QUERY_EVENT:databaseName:{},tableName:{}", databaseName, tableName);

                CDCEvent cdcEvent = new CDCEvent(qe, databaseName, tableName);
                cdcEvent.setDdl(true);
                cdcEvent.setSql(qe.getSql().toString());

                CDCEventManager.queue.addLast(cdcEvent);
                logger.info("cdcEvent:{}", cdcEvent);
                DATABASE_TABLE.put(tableInfo, "query");
                break;
            }
            //在事务提交时，不管是STATEMENT还是ROW格式的binlog，都会在末尾添加一个XID_EVENT事件代表事务的结束。该事件记录了
            // 该事务的ID，在MySQL进行崩溃恢复时，根据事务在binlog中的提交情况来决定是否提交存储引擎中状态为prepared的事务。
            case MySQLConstants.XID_EVENT:
            {
                XidEvent xe = (XidEvent)be;
                logger.trace("XID_EVENT: xid:{}", xe.getXid());
                break;
            }
            default:
            {
                logger.trace("DEFAULT:{}", eventType);
                break;
            }
        }

    }

    /**
     * ROW_EVENT中是没有Column信息的，需要通过MysqlConnection（下面会讲到）的方式读取列名信息，
     * 然后跟取回的List<Column>进行映射。
     *
     * @param cols
     * @param databaseName
     * @param tableName
     * @return
     */
    private Map<String, String> getMap(List<Column> cols, String databaseName, String tableName)
    {
        Map<String, String> map = new HashMap<>();
        if (cols == null || cols.size() == 0)
        {
            return null;
        }

        String fullName = databaseName + "." + tableName;
        List<ColumnInfo> columnInfoList = TableInfoKeeper.getColumns(fullName);
        if (columnInfoList == null)
            return null;
        if (columnInfoList.size() != cols.size())
        {
            TableInfoKeeper.refreshColumnsMap();
            if (columnInfoList.size() != cols.size())
            {
                logger.warn("columnInfoList.size is not equal to cols.");
                return null;
            }
        }

        for (int i = 0; i < columnInfoList.size(); i++)
        {
            if (cols.get(i).getValue() == null)
                map.put(columnInfoList.get(i).getName(), "");
            else
                map.put(columnInfoList.get(i).getName(), cols.get(i).toString());
        }

        return map;
    }

    /**
     * 从sql中提取Table信息，因为QUERY_EVENT是对应DATABASE这一级别的，不像ROW_EVENT是对应TABLE这一级别的，
     * 所以需要通过从sql中提取TABLE信息,封装到TableInfo对象中
     *
     * @param qe
     * @return
     */
    private TableInfo createTableInfo(QueryEvent qe)
    {
        //String sql = qe.getSql().toString().toLowerCase();
        //TableInfo ti = new TableInfo();
        //String databaseName = qe.getDatabaseName().toString();
        //String tableName = null;
        //if (checkFlag(sql, "table"))
        //{
        //    tableName = getTableName(sql, "table");
        //}
        //else if (checkFlag(sql, "truncate"))
        //{
        //    tableName = getTableName(sql, "truncate");
        //}
        //else
        //{
        //    logger.warn("can not find table name from sql:{}", sql);
        //    return null;
        //}
        //ti.setDatabaseName(databaseName);
        //ti.setTableName(tableName);
        //ti.setFullName(databaseName + "." + tableName);

        String tableName;
        String sql = qe.getSql().toString().toLowerCase().trim();
        if (sql.indexOf("delete") == 0)
        {
            String temp = sql.substring(sql.indexOf("from"), sql.indexOf("where")).trim();
            tableName = temp.substring(temp.lastIndexOf(" ") + 1);
        }
        else if (sql.indexOf("update") == 0)
        {
            String temp = sql.substring(sql.indexOf("update"), sql.indexOf("set")).trim();
            tableName = temp.substring(temp.lastIndexOf(" ") + 1);
        }
        else if (sql.indexOf("insert") == 0)
        {
            String temp = sql.substring(sql.indexOf("into"), sql.indexOf("(")).trim();
            tableName = temp.substring(temp.lastIndexOf(" ") + 1);
        }
        else if (sql.indexOf("drop") == 0)
        {
            String temp = sql.substring(sql.indexOf("table") + 5).trim();
            tableName = temp.substring(0, temp.indexOf("`", 1) + 1);
        }
        else if (sql.indexOf("truncate") == 0)
        {
            tableName = sql.substring(sql.indexOf("table") + 5).trim();
        }
        else if (sql.indexOf("create") == 0)
        {
            String temp = sql.substring(sql.indexOf("table"), sql.indexOf("(")).trim();
            tableName = temp.substring(temp.lastIndexOf(" ") + 1);
        }
        else if (sql.indexOf("alter") == 0)
        {
            String temp = sql.substring(sql.indexOf("table") + 5).trim();
            tableName = temp.substring(0, temp.indexOf("`", 1) + 1);
        }
        else
        {
            return null;
        }
        TableInfo ti = new TableInfo();
        String databaseName = qe.getDatabaseName().toString();
        ti.setDatabaseName(databaseName);
        if (tableName.contains("."))
        {
            ti.setFullName(databaseName + "." + tableName);
        }
        else
        {
            ti.setFullName(tableName);
        }

        ti.setTableName(tableName);
        return ti;
    }

    private boolean checkFlag(String sql, String flag)
    {
        String[] ss = sql.split(" ");
        for (String s : ss)
        {
            if (s.equals(flag))
            {
                return true;
            }
        }
        return false;
    }

    private String getTableName(String sql, String flag)
    {
        String[] ss = sql.split("\\.");
        String tName = null;
        if (ss.length > 1)
        {
            String[] strs = ss[1].split(" ");
            tName = strs[0];
        }
        else
        {
            String[] strs = sql.split(" ");
            boolean start = false;
            for (String s : strs)
            {
                if (s.indexOf(flag) >= 0)
                {
                    start = true;
                    continue;
                }
                if (start && !s.isEmpty())
                {
                    tName = s;
                    break;
                }
            }
        }
        tName.replaceAll("`", "").replaceAll(";", "");

        //del "("[create table person(....]
        int index = tName.indexOf('(');
        if (index > 0)
        {
            tName = tName.substring(0, index);
        }

        return tName;
    }
}
