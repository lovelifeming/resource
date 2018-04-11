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


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/9.
 * @Modified By:
 */
public class InstanceListener implements BinlogEventListener
{
    private static final Logger logger = LoggerFactory.getLogger(InstanceListener.class);

    @Override
    public void onEvents(BinlogEventV4 be) {
        if(be == null){
            logger.error("binlog event is null");
            return;
        }

        int eventType = be.getHeader().getEventType();
        switch(eventType){
            case MySQLConstants.FORMAT_DESCRIPTION_EVENT:
            {
                logger.trace("FORMAT_DESCRIPTION_EVENT");
                break;
            }
            case MySQLConstants.TABLE_MAP_EVENT://每次ROW_EVENT前都伴随一个TABLE_MAP_EVENT事件，保存一些表信息，如tableId, tableName, databaseName, 而ROW_EVENT只有tableId
            {
                TableMapEvent tme = (TableMapEvent)be;
                TableInfoKeeper.saveTableIdMap(tme);
                logger.trace("TABLE_MAP_EVENT:tableId:{}",tme.getTableId());
                break;
            }
            case MySQLConstants.DELETE_ROWS_EVENT:
            {
                DeleteRowsEvent dre = (DeleteRowsEvent) be;
                long tableId = dre.getTableId();
                logger.trace("DELETE_ROW_EVENT:tableId:{}",tableId);

                TableInfo tableInfo = TableInfoKeeper.getTableInfo(tableId);
                String databaseName = tableInfo.getDatabaseName();
                String tableName = tableInfo.getTableName();

                List<Row> rows = dre.getRows();
                for(Row row:rows){
                    List<Column> before = row.getColumns();
                    Map<String,String> beforeMap = getMap(before,databaseName,tableName);
                    if(beforeMap !=null && beforeMap.size()>0){
                        CDCEvent cdcEvent = new CDCEvent(dre,databaseName,tableName);
                        cdcEvent.setDdl(false);
                        cdcEvent.setSql(null);
                        cdcEvent.setBefore(beforeMap);
                        CDCEventManager.queue.addLast(cdcEvent);
                        logger.info("cdcEvent:{}",cdcEvent);
                    }
                }
                break;
            }
            case MySQLConstants.UPDATE_ROWS_EVENT:
            {
                UpdateRowsEvent upe = (UpdateRowsEvent)be;
                long tableId = upe.getTableId();
                logger.info("UPDATE_ROWS_EVENT:tableId:{}",tableId);

                TableInfo tableInfo = TableInfoKeeper.getTableInfo(tableId);
                String databaseName = tableInfo.getDatabaseName();
                String tableName = tableInfo.getTableName();

                List<Pair<Row>> rows = upe.getRows();
                for(Pair<Row> p:rows){
                    List<Column> colsBefore = p.getBefore().getColumns();
                    List<Column> colsAfter = p.getAfter().getColumns();

                    Map<String,String> beforeMap = getMap(colsBefore,databaseName,tableName);
                    Map<String,String> afterMap = getMap(colsAfter,databaseName,tableName);
                    if(beforeMap!=null && afterMap!=null && beforeMap.size()>0 && afterMap.size()>0){
                        CDCEvent cdcEvent = new CDCEvent(upe,databaseName,tableName);
                        cdcEvent.setDdl(false);
                        cdcEvent.setSql(null);
                        cdcEvent.setBefore(beforeMap);
                        cdcEvent.setAfter(afterMap);
                        CDCEventManager.queue.addLast(cdcEvent);
                        logger.info("cdcEvent:{}",cdcEvent);
                    }
                }
                break;
            }
            case MySQLConstants.WRITE_ROWS_EVENT:
            {
                WriteRowsEvent wre = (WriteRowsEvent)be;
                long tableId = wre.getTableId();
                logger.trace("WRITE_ROWS_EVENT:tableId:{}",tableId);

                TableInfo tableInfo = TableInfoKeeper.getTableInfo(tableId);
                String databaseName = tableInfo.getDatabaseName();
                String tableName = tableInfo.getTableName();

                List<Row> rows = wre.getRows();
                for(Row row: rows){
                    List<Column> after = row.getColumns();
                    Map<String,String> afterMap = getMap(after,databaseName,tableName);
                    if(afterMap!=null && afterMap.size()>0){
                        CDCEvent cdcEvent = new CDCEvent(wre,databaseName,tableName);
                        cdcEvent.setDdl(false);
                        cdcEvent.setSql(null);
                        cdcEvent.setAfter(afterMap);
                        CDCEventManager.queue.addLast(cdcEvent);
                        logger.info("cdcEvent:{}",cdcEvent);
                    }
                }
                break;
            }
            case MySQLConstants.QUERY_EVENT:
            {
                QueryEvent qe = (QueryEvent)be;
                TableInfo tableInfo = createTableInfo(qe);
                if(tableInfo == null)
                    break;
                String databaseName = tableInfo.getDatabaseName();
                String tableName = tableInfo.getTableName();
                logger.trace("QUERY_EVENT:databaseName:{},tableName:{}",databaseName,tableName);

                CDCEvent cdcEvent = new CDCEvent(qe,databaseName,tableName);
                cdcEvent.setDdl(true);
                cdcEvent.setSql(qe.getSql().toString());

                CDCEventManager.queue.addLast(cdcEvent);
                logger.info("cdcEvent:{}",cdcEvent);

                break;
            }
            case MySQLConstants.XID_EVENT:{
                XidEvent xe = (XidEvent)be;
                logger.trace("XID_EVENT: xid:{}",xe.getXid());
                break;
            }
            default:
            {
                logger.trace("DEFAULT:{}",eventType);
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
    private Map<String,String> getMap(List<Column> cols, String databaseName, String tableName){
        Map<String,String> map = new HashMap<>();
        if(cols == null || cols.size()==0){
            return null;
        }

        String fullName = databaseName+"."+tableName;
        List<ColumnInfo> columnInfoList = TableInfoKeeper.getColumns(fullName);
        if(columnInfoList == null)
            return null;
        if(columnInfoList.size() != cols.size()){
            TableInfoKeeper.refreshColumnsMap();
            if(columnInfoList.size() != cols.size())
            {
                logger.warn("columnInfoList.size is not equal to cols.");
                return null;
            }
        }

        for(int i=0;i<columnInfoList.size(); i++){
            if(cols.get(i).getValue()==null)
                map.put(columnInfoList.get(i).getName(),"");
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
    private TableInfo createTableInfo(QueryEvent qe){
        String sql = qe.getSql().toString().toLowerCase();

        TableInfo ti = new TableInfo();
        String databaseName = qe.getDatabaseName().toString();
        String tableName = null;
        if(checkFlag(sql,"table")){
            tableName = getTableName(sql,"table");
        } else if(checkFlag(sql,"truncate")){
            tableName = getTableName(sql,"truncate");
        } else{
            logger.warn("can not find table name from sql:{}",sql);
            return null;
        }
        ti.setDatabaseName(databaseName);
        ti.setTableName(tableName);
        ti.setFullName(databaseName+"."+tableName);

        return ti;
    }

    private boolean checkFlag(String sql, String flag){
        String[] ss = sql.split(" ");
        for(String s:ss){
            if(s.equals(flag)){
                return true;
            }
        }
        return false;
    }

    private String getTableName(String sql, String flag){
        String[] ss = sql.split("\\.");
        String tName = null;
        if (ss.length > 1) {
            String[] strs = ss[1].split(" ");
            tName = strs[0];
        } else {
            String[] strs = sql.split(" ");
            boolean start = false;
            for (String s : strs) {
                if (s.indexOf(flag) >= 0) {
                    start = true;
                    continue;
                }
                if (start && !s.isEmpty()) {
                    tName = s;
                    break;
                }
            }
        }
        tName.replaceAll("`", "").replaceAll(";", "");

        //del "("[create table person(....]
        int index = tName.indexOf('(');
        if(index>0){
            tName = tName.substring(0, index);
        }

        return tName;
    }
}
