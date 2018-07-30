package com.zsm.bigdata.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/2/8 10:01.
 * @Modified By:
 */
public class HBaseFilter
{
    private static Configuration conf = null;

    static
    {
        // 初始化配置
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "localhost");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
    }

    /**
     * 筛选出匹配的所有的行
     * 使用BinaryComparator可以筛选出具有某个行键的行，或者通过改变比较运算符来筛选出符合某一条件的多条数据
     *
     * @param tableName
     * @param rowKey
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject rowFilter(String tableName, String rowKey)
        throws IOException, JSONException
    {
        // 筛选出与对应rowKey相等的行
        Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(rowKey)));
        return hbasFilter(tableName, filter);
    }

    /**
     * 筛选出具有特定前缀的行键的数据
     *
     * @param tableName
     * @param rowKey
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject prefixFilter(String tableName, String rowKey)
        throws IOException, JSONException
    {
        // 筛选匹配行键前缀成功的行
        Filter filter = new PrefixFilter(Bytes.toBytes(rowKey));
        return hbasFilter(tableName, filter);
    }

    /**
     * 筛选每行的行键值，过滤掉行里面的值
     *
     * @param tableName
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject keyOnlyFilter(String tableName)
        throws IOException, JSONException
    {
        // 返回所有的行键，但值全是空
        Filter filter = new KeyOnlyFilter();
        return hbasFilter(tableName, filter);
    }

    /**
     * 按照一定的几率（0~1之间，<=0会过滤掉所有的行，>=1会包含所有的行）来返回随机的结果集
     *
     * @param tableName
     * @param chance
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject randomRowFilter(String tableName, float chance)
        throws IOException, JSONException
    {
        // 随机选出一部分的行
        Filter filter = new RandomRowFilter(chance);
        return hbasFilter(tableName, filter);
    }

    /**
     * 行键的返回是前闭后开区间，即包含起始行，但不包含终止行
     *
     * @param tableName
     * @param rowKey
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject inclusiveStopFilter(String tableName, String rowKey)
        throws IOException, JSONException
    {
        // 结果包含了扫描的上限
        Filter filter = new InclusiveStopFilter(Bytes.toBytes(rowKey));
        return hbasFilter(tableName, filter);
    }

    /**
     * 获取只包含第一列的数据
     *
     * @param tableName
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject firstKeyOnlyFilter(String tableName)
        throws IOException, JSONException
    {
        // 筛选出每行的第一个单元格
        Filter filter = new FirstKeyOnlyFilter();
        return hbasFilter(tableName, filter);
    }

    /**
     * 按照列名的前缀来筛选单元格
     *
     * @param tableName
     * @param columnPrefix
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject columPrefixFilter(String tableName, String columnPrefix)
        throws IOException, JSONException
    {
        // 筛选出前缀匹配的列
        Filter filter = new ColumnPrefixFilter(Bytes.toBytes(columnPrefix));
        return hbasFilter(tableName, filter);
    }

    /**
     * 按照具体的值来筛选单元格
     *
     * @param tableName
     * @param value
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject valueFilter(String tableName, String value)
        throws IOException, JSONException
    {
        // 筛选某个满足条件的特定单元格
        Filter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(value));
        return hbasFilter(tableName, filter);
    }

    /**
     * 在遇到一行的列数超过我们所设置的限制值的时候，结束扫描操作
     *
     * @param tableName
     * @param columnCount
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject columnCountFilter(String tableName, int columnCount)
        throws IOException, JSONException
    {
        // 如果突然发现一行中的列数超过设定的最大值时，整个扫描操作会停止
        Filter filter = new ColumnCountGetFilter(columnCount);
        return hbasFilter(tableName, filter);
    }

    /**
     * 筛选列的值判断这一行的数据是否被过滤
     *
     * @param tableName
     * @param columnFamily    列族
     * @param qualifier       列名
     * @param value           比较值
     * @param compareOp       比较类型，EQUA 等于；NOT_EQUAL 不等于;LESS 小于,GREATER  大于
     * @param filterIfMissing 是否保留筛选列，false 筛选列保留，true 筛选列不保留
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject singleColumnValueFilter(String tableName, String columnFamily, String qualifier,
                                                     String value, CompareFilter.CompareOp compareOp,
                                                     boolean filterIfMissing)
        throws IOException, JSONException
    {
        // 将满足条件的列所在的行过滤掉
        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(columnFamily),
            Bytes.toBytes(qualifier), compareOp, new SubstringComparator(value));
        //setFilterIfMissing方法，默认的参数是false。
        // 如果参数为true，满足条件的行将会被移除结果集，如果参数为false，满足条件的行会包含在结果集中。
        filter.setFilterIfMissing(filterIfMissing);
        return hbasFilter(tableName, filter);
    }

    /**
     * 筛选列的值过滤这一行的数据
     *
     * @param tableName
     * @param columnFamily 列族
     * @param qualifier    列名
     * @param value        比较值
     * @param compareOp    比较类型，EQUA 等于；NOT_EQUAL 不等于;LESS 小于,GREATER  大于
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject singleColumnValueExcludeFilter(String tableName, String columnFamily, String qualifier,
                                                            String value, CompareFilter.CompareOp compareOp)
        throws IOException, JSONException
    {
        // 将满足条件的列所在的行过滤掉
        SingleColumnValueExcludeFilter filter = new SingleColumnValueExcludeFilter(Bytes.toBytes(columnFamily),
            Bytes.toBytes(qualifier), compareOp, new SubstringComparator(value));
        return hbasFilter(tableName, filter);
    }

    /**
     * 附加过滤器,其与ValueFilter结合使用，如果发现一行中的某一列不符合条件，那么整行就会被过滤掉。
     *
     * @param tableName
     * @param filter
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject skipFilter(String tableName, Filter filter)
        throws IOException, JSONException
    {
        // 如果一行中的一列不满足条件需要过滤时，整个行就会被过滤掉
        Filter skipFilter = new SkipFilter(filter);
        return hbasFilter(tableName, skipFilter);
    }

    /**
     * 当遇到不符合设定条件的数据的时候，整个扫描结束
     *
     * @param tableName
     * @param filter
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject whileMatchFilter(String tableName, Filter filter)
        throws IOException, JSONException
    {
        // 当遇到不符合过滤器filter设置的条件时，整个扫描结束
        Filter skipFilter = new WhileMatchFilter(filter);
        return hbasFilter(tableName, skipFilter);
    }

    /**
     * 综合使用多个过滤器,两种关系:Operator.MUST_PASS_ONE表示关系AND，Operator.MUST_PASS_ALL表示关系OR
     *
     * @param tableName
     * @param filter
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject filterListFilter(String tableName, FilterList.Operator operator, Filter... filter)
        throws IOException, JSONException
    {
        // 综合使用多个过滤器，AND和OR两种关系
        FilterList filters = new FilterList(operator, filter);
        return hbasFilter(tableName, filters);
    }

    /**
     * HBase过滤操作
     *
     * @param tableName
     * @param filter
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject hbasFilter(String tableName, Filter filter)
        throws IOException, JSONException
    {
        // 建立一个数据库的连接
        Connection conn = ConnectionFactory.createConnection(conf);
        // 获取表
        HTable table = (HTable)conn.getTable(TableName.valueOf(tableName));
        // 创建一个扫描对象
        Scan scan = new Scan();
        // 将过滤器加入扫描对象
        scan.setFilter(filter);
        // 获取扫描结果
        ResultScanner results = table.getScanner(scan);
        JSONObject json = HBaseOperator.convertResultScanner(results);
        results.close();
        conn.close();
        return json;
    }
}
