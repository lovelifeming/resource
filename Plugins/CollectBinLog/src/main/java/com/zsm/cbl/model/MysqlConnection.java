package com.zsm.cbl.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/9.
 * @Modified By:
 */
public class MysqlConnection
{
    private static final Logger logger = LoggerFactory.getLogger(MysqlConnection.class);

    private static Connection conn;

    private static String host;
    private static int port;
    private static String user;
    private static String password;

    public static void setConnection(String hostArg, int portArg, String userArg, String passwordArg){
        try {
            if(conn == null || conn.isClosed()){
                Class.forName("com.mysql.jdbc.Driver");

                host = hostArg;
                port = portArg;
                user = userArg;
                password = passwordArg;

                conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/",user,password);
                logger.info("connected to mysql:{} : {}",user,password);
            }
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(),e);
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }
    }

    public static Connection getConnection(){
        try {
            if(conn == null || conn.isClosed()){
                setConnection(host,port,user,password);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }
        return conn;
    }

    /**
     * 获取Column信息
     *
     * @return
     */
    public static Map<String,List<ColumnInfo>> getColumns(){
        Map<String,List<ColumnInfo>> cols = new HashMap<>();
        Connection conn = getConnection();

        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet r = metaData.getCatalogs();
            String tableType[] = {"TABLE"};
            while(r.next()){
                String databaseName = r.getString("TABLE_CAT");
                ResultSet result = metaData.getTables(databaseName, null, null, tableType);
                while(result.next()){
                    String tableName = result.getString("TABLE_NAME");
//                  System.out.println(result.getInt("TABLE_ID"));
                    String key = databaseName +"."+tableName;
                    ResultSet colSet = metaData.getColumns(databaseName, null, tableName, null);
                    cols.put(key, new ArrayList<ColumnInfo>());
                    while(colSet.next()){
                        ColumnInfo columnInfo = new ColumnInfo(colSet.getString("COLUMN_NAME"),colSet.getString("TYPE_NAME"));
                        cols.get(key).add(columnInfo);
                    }

                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }
        return cols;
    }

    /**
     * 参考
     * mysql> show binary logs
     *  +------------------+-----------+
     *  | Log_name         | File_size |
     *  +------------------+-----------+
     *  | mysql-bin.000001 |       126 |
     *  | mysql-bin.000002 |       126 |
     *  | mysql-bin.000003 |      6819 |
     *  | mysql-bin.000004 |      1868 |
     *  +------------------+-----------+
     */
    public static List<BinlogInfo> getBinlogInfo(){
        List<BinlogInfo> binlogList = new ArrayList<>();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery("show binary logs");
            while(resultSet.next()){
                BinlogInfo binlogInfo = new BinlogInfo(resultSet.getString("Log_name"),resultSet.getLong("File_size"));
                binlogList.add(binlogInfo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        } finally{
            try {
                if(resultSet != null)
                    resultSet.close();
                if(statement != null)
                    statement.close();
                if(conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(),e);
            }
        }

        return binlogList;
    }

    /**
     * 参考：
     * mysql> show master status;
     *  +------------------+----------+--------------+------------------+
     *  | File             | Position | Binlog_Do_DB | Binlog_Ignore_DB |
     *  +------------------+----------+--------------+------------------+
     *  | mysql-bin.000004 |     1868 |              |                  |
     *  +------------------+----------+--------------+------------------+
     * @return
     */
    public static BinlogMasterStatus getBinlogMasterStatus(){
        BinlogMasterStatus binlogMasterStatus = new BinlogMasterStatus();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery("show master status");
//            resultSet = statement.executeQuery("show slave status");
            while(resultSet.next()){
                binlogMasterStatus.setBinlogName(resultSet.getString("File"));
                binlogMasterStatus.setPosition(resultSet.getLong("Position"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        } finally{
            try {
                if(resultSet != null)
                    resultSet.close();
                if(statement != null)
                    statement.close();
                if(conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(),e);
            }
        }

        return binlogMasterStatus;
    }

    /**
     * 获取open-replicator所连接的mysql服务器的serverid信息
     * @return
     */
    public static int getServerId(){
        int serverId=6789;
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery("show variables like 'server_id'");
            while(resultSet.next()){
                serverId = resultSet.getInt("Value");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        } finally{
            try {
                if(resultSet != null)
                    resultSet.close();
                if(statement != null)
                    statement.close();
                if(conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(),e);
            }
        }

        return serverId;
    }
}
