package com.zsm.bigdata.hbase;

import com.zsm.bigdata.util.Utils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Java实现HBase表操作,远程连接HBASE需要配置 hbase-site.xml,在集群获取hbase-site.xml配置文件
 * 1.初始化配置；
 * 2.建立数据库连接；
 * 3.获取表；
 * 4.创建操作参数对象，设置参数；
 * 5.执行操作输出结果。
 *
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/2/7 14:34.
 * @Modified By:
 */
public class HBaseOperator
{
    private static Configuration conf = null;

    static
    {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "localhost");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
    }

    /**
     * 创建数据库表
     *
     * @param tableName
     * @param columnFamily
     * @return
     */
    public static boolean createTable(String tableName, String[] columnFamily)
    {
        Connection conn = null;
        try
        {
            // 建立一个数据库的连接
            conn = ConnectionFactory.createConnection(conf);
            // 创建一个数据库管理员
            HBaseAdmin admin = (HBaseAdmin)conn.getAdmin();
            if (admin.tableExists(TableName.valueOf(tableName)))
            {
                return false;
            }
            else
            {
                // 新建一个表描述
                HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
                for (String col : columnFamily)
                {
                    // 在表描述里添加列族
                    tableDesc.addFamily(new HColumnDescriptor(col));
                }
                // 根据配置好的表描述建表
                admin.createTable(tableDesc);
                return true;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            Utils.closeStream(conn);
        }
    }

    /**
     * 添加一条数据
     *
     * @param tableName
     * @param rowKey
     * @param columnFamily
     * @param column
     * @param value
     * @return
     */
    public static boolean insert(String tableName, String rowKey, String columnFamily, String column, String value)
    {
        Connection conn = null;
        HTable table = null;
        try
        {
            // 建立一个数据库的连接
            conn = ConnectionFactory.createConnection(conf);
            if (!conn.getAdmin().tableExists(TableName.valueOf(tableName)))
            {
                return false;
            }
            // 获取表
            table = (HTable)conn.getTable(TableName.valueOf(tableName));
            // 通过rowkey创建一个put对象
            Put put = new Put(Bytes.toBytes(rowKey));
            // 在put对象中设置列族、列、值
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));
            // 插入数据,可通过put(List<Put>)批量插入
            table.put(put);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            Utils.closeStream(table, conn);
        }
    }

    /**
     * 添加多条数据
     *
     * @param tableName
     * @param rowKey
     * @param columnFamily
     * @param column
     * @param value
     * @return
     */
    public static boolean insertList(String tableName, String[] rowKey, String[] columnFamily, String[] column,
                                     String[] value)
    {
        try (Connection conn = ConnectionFactory.createConnection(conf);
             HTable table = (HTable)conn.getTable(TableName.valueOf(tableName)))
        {
            List<Put> putList = new ArrayList<>();
            int length = rowKey.length;
            for (int i = 0; i < length; i++)
            {
                // 通过rowkey创建一个put对象
                Put put = new Put(Bytes.toBytes(rowKey[i]));
                // 在put对象中设置列族、列、值
                put.addColumn(Bytes.toBytes(columnFamily[i]), Bytes.toBytes(column[i]), Bytes.toBytes(value[i]));
            }
            // 插入数据,可通过put(List<Put>)批量插入
            table.put(putList);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过rowkey获取一条数据
     *
     * @param tableName
     * @param rowKey
     * @return
     */
    public static JSONObject getRow(String tableName, String rowKey)
    {
        Connection conn = null;
        HTable table = null;
        JSONObject json = new JSONObject();
        try
        {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable)conn.getTable(TableName.valueOf(tableName));
            // 通过rowkey创建一个get对象
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);
            Cell[] cells = result.rawCells();
            List<Map> list = new ArrayList<>();
            for (int i = 0; i < cells.length; i++)
            {
                Cell cell = cells[i];
                Map temp = new HashMap<String, String>();
                temp.put("rowkey", new String(CellUtil.cloneRow(cell)));
                temp.put("columnfamily", new String(CellUtil.cloneFamily(cell)));
                temp.put("columnname", new String(CellUtil.cloneQualifier(cell)));
                temp.put("value", new String(CellUtil.cloneValue(cell)));
                temp.put("timestamp", cell.getTimestamp());
                list.add(temp);
            }
            json.put(Integer.toString(0), list);
            return json;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return json;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return json;
        }
        finally
        {
            Utils.closeStream(table, conn);
        }
    }

    /**
     * 获取全表信息
     *
     * @param tableName
     * @return
     */
    public static JSONObject getAllRow(String tableName)
    {
        Connection conn = null;
        HTable table = null;
        JSONObject json = new JSONObject();
        try
        {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable)conn.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            // 扫描全表输出结果
            ResultScanner results = table.getScanner(scan);
            json = convertResultScanner(results);
            return json;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return json;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return json;
        }
        finally
        {
            Utils.closeStream(table, conn);
        }
    }

    /**
     * 把ResultScanner转换为JSONObject
     *
     * @param results
     * @return
     * @throws JSONException
     */
    public static JSONObject convertResultScanner(ResultScanner results)
        throws JSONException
    {
        int index = 0;
        JSONObject json = new JSONObject();
        for (Result r : results)
        {
            List<Map> list = new ArrayList<>();
            for (Cell cell : r.rawCells())
            {
                Map temp = new HashMap<String, String>();
                temp.put("rowkey", new String(CellUtil.cloneRow(cell)));
                temp.put("columnfamily", new String(CellUtil.cloneFamily(cell)));
                temp.put("columnname", new String(CellUtil.cloneQualifier(cell)));
                temp.put("value", new String(CellUtil.cloneValue(cell)));
                temp.put("timestamp", cell.getTimestamp());
                list.add(temp);
            }
            json.put(Integer.toString(index++), list);
        }
        return json;
    }

    /**
     * 删除一条数据
     *
     * @param tableName
     * @param rowKey
     * @return
     */
    public static boolean deleteRow(String tableName, String rowKey)
    {
        return deleteRows(tableName, rowKey);
    }

    /**
     * 删除多条数据
     *
     * @param tableName
     * @param rowKey
     * @return
     */
    public static boolean deleteRows(String tableName, String... rowKey)
    {
        Connection conn = null;
        HTable table = null;
        try
        {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable)conn.getTable(TableName.valueOf(tableName));
            // 删除多条数据
            List<Delete> list = new ArrayList<Delete>();
            for (String key : rowKey)
            {
                list.add(new Delete(Bytes.toBytes(key)));
            }
            // 删除数据
            table.delete(list);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            Utils.closeStream(table, conn);
        }
    }

    /**
     * 删除列族
     *
     * @param tableName
     * @param columnFaily
     * @return
     */
    public static boolean deleteColumnFamily(String tableName, String columnFaily)
    {
        Connection conn = null;
        HTable table = null;
        try
        {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable)conn.getTable(TableName.valueOf(tableName));
            // 创建一个数据库管理员
            HBaseAdmin admin = (HBaseAdmin)conn.getAdmin();
            // 删除一个表的指定列族
            admin.deleteColumnFamily(TableName.valueOf(tableName),columnFaily.getBytes());
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            Utils.closeStream(table, conn);
        }
    }

    /**
     * 删除数据库表
     *
     * @param tableName
     * @return
     */
    public static boolean deleteTable(String tableName)
    {
        Connection conn = null;
        try
        {
            conn = ConnectionFactory.createConnection(conf);
            HBaseAdmin admin = (HBaseAdmin)conn.getAdmin();
            if (admin.tableExists(TableName.valueOf(tableName)))
            {
                // 失效表
                admin.disableTable(TableName.valueOf(tableName));
                // 失效表
                admin.deleteTable(TableName.valueOf(tableName));
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            Utils.closeStream(conn);
        }
    }

    /**
     * 追加插入(将原有value的后面追加新的value，如原有value=a追加value=bc则最后的value=abc)
     *
     * @param tableName
     * @param rowKey
     * @param columnFamily
     * @param column
     * @param value
     * @return
     */
    public static boolean appendData(String tableName, String rowKey, String columnFamily, String column, String value)
    {
        Connection conn = null;
        HTable table = null;
        try
        {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable)conn.getTable(TableName.valueOf(tableName));
            // 通过rowkey创建一个append对象
            Append append = new Append(Bytes.toBytes(rowKey));
            // 通过rowkey创建一个append对象
            append.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));
            // 追加数据
            table.append(append);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            Utils.closeStream(table, conn);
        }
    }

    /**
     * 符合条件后添加数据(只能针对某一个rowkey进行原子操作)
     *
     * @param tableName
     * @param rowKey
     * @param columnFamilyCheck
     * @param columnCheck
     * @param valueCheck
     * @param columnFamily
     * @param column
     * @param value
     * @return
     */
    public static boolean checkAndPut(String tableName, String rowKey, String columnFamilyCheck, String columnCheck,
                                      String valueCheck, String columnFamily, String column, String value)
    {
        Connection conn = null;
        try
        {
            conn = ConnectionFactory.createConnection(conf);
            HTable table = (HTable)conn.getTable(TableName.valueOf(tableName));
            // 设置需要添加的数据
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));
            // 当判断条件为真时添加数据
            boolean result = table.checkAndPut(Bytes.toBytes(rowKey), Bytes.toBytes(columnFamilyCheck),
                Bytes.toBytes(columnCheck), Bytes.toBytes(valueCheck), put);
            return result;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            Utils.closeStream(conn);
        }
    }

    /**
     * 符合条件后刪除数据(只能针对某一个rowkey进行原子操作)
     *
     * @param tableName
     * @param rowKey
     * @param columnFamilyCheck
     * @param columnCheck
     * @param valueCheck
     * @param columnFamily
     * @param column
     * @param value
     * @return
     */
    public static boolean checkAndDelete(String tableName, String rowKey, String columnFamilyCheck, String columnCheck,
                                         String valueCheck, String columnFamily, String column, String value)
    {
        Connection conn = null;
        HTable table = null;
        try
        {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable)conn.getTable(TableName.valueOf(tableName));
            // 设置需要刪除的delete对象
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumn(Bytes.toBytes(columnFamilyCheck), Bytes.toBytes(columnCheck));
            // 当判断条件为真时添加数据
            boolean result = table.checkAndDelete(Bytes.toBytes(rowKey), Bytes.toBytes(columnFamilyCheck),
                Bytes.toBytes(columnCheck),
                Bytes.toBytes(valueCheck), delete);
            return result;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            Utils.closeStream(table, conn);
        }
    }

    /**
     * 计数器(amount为正数则计数器加，为负数则计数器减，为0则获取当前计数器的值)
     *
     * @param tableName
     * @param rowKey
     * @param columnFamily
     * @param column
     * @param amount
     * @return
     */
    public static long incrementColumnValue(String tableName, String rowKey, String columnFamily, String column,
                                            long amount)
    {
        Connection conn = null;
        HTable table = null;
        try
        {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable)conn.getTable(TableName.valueOf(tableName));
            long result = table.incrementColumnValue(Bytes.toBytes(rowKey), Bytes.toBytes(columnFamily),
                Bytes.toBytes(column), amount);
            return result;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return -1L;
        }
        finally
        {
            Utils.closeStream(table, conn);
        }
    }
}
