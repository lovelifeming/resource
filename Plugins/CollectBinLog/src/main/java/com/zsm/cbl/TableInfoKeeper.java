package com.zsm.cbl;

import com.google.code.or.binlog.impl.event.TableMapEvent;
import com.zsm.cbl.model.ColumnInfo;
import com.zsm.cbl.model.MysqlConnection;
import com.zsm.cbl.model.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/9.
 * @Modified By:
 */
public class TableInfoKeeper
{
    private static final Logger logger = LoggerFactory.getLogger(TableInfoKeeper.class);

    private static Map<Long,TableInfo> tabledIdMap = new ConcurrentHashMap<>();
    private static Map<String,List<ColumnInfo>> columnsMap = new ConcurrentHashMap<>();

    static{
        columnsMap = MysqlConnection.getColumns();
    }

    public static void saveTableIdMap(TableMapEvent tme){
        long tableId = tme.getTableId();
        tabledIdMap.remove(tableId);

        TableInfo table = new TableInfo();
        table.setDatabaseName(tme.getDatabaseName().toString());
        table.setTableName(tme.getTableName().toString());
        table.setFullName(tme.getDatabaseName()+"."+tme.getTableName());

        tabledIdMap.put(tableId, table);
    }

    public static synchronized void refreshColumnsMap(){
        Map<String,List<ColumnInfo>> map = MysqlConnection.getColumns();
        if(map.size()>0){
//          logger.warn("refresh and clear cols.");
            columnsMap = map;
//          logger.warn("refresh and switch cols:{}",map);
        }
        else
        {
            logger.error("refresh columnsMap error.");
        }
    }

    public static TableInfo getTableInfo(long tableId){
        return tabledIdMap.get(tableId);
    }

    public static List<ColumnInfo> getColumns(String fullName){
        return columnsMap.get(fullName);
    }
}
