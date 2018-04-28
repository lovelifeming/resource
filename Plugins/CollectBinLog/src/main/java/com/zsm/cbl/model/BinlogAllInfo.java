package com.zsm.cbl.model;

/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/27.
 * @Modified By:
 */
public class BinlogAllInfo
{
    private String databaseName;

    private String tableName;

    private String fullName;

    private String sql;

    private String binlogName;

    private long position;

    private Long fileSize;

    public String getDatabaseName()
    {
        return databaseName;
    }

    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getSql()
    {
        return sql;
    }

    public void setSql(String sql)
    {
        this.sql = sql;
    }

    public String getBinlogName()
    {
        return binlogName;
    }

    public void setBinlogName(String binlogName)
    {
        this.binlogName = binlogName;
    }

    public long getPosition()
    {
        return position;
    }

    public void setPosition(long position)
    {
        this.position = position;
    }

    public Long getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    @Override
    public String toString()
    {
        return "BinlogAllInfo{" +
               "databaseName='" + databaseName + '\'' +
               ", tableName='" + tableName + '\'' +
               ", fullName='" + fullName + '\'' +
               ", sql='" + sql + '\'' +
               ", binlogName='" + binlogName + '\'' +
               ", position=" + position +
               ", fileSize=" + fileSize +
               '}';
    }
}
