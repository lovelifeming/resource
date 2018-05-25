package com.zsm.cbl.model;

/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/9.
 * @Modified By:
 */
public class TableInfo
{
    private String databaseName;

    private String tableName;

    private String fullName;

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        TableInfo tableInfo = (TableInfo)o;
        if (!this.databaseName.equals(tableInfo.getDatabaseName()))
            return false;
        if (!this.tableName.equals(tableInfo.getTableName()))
            return false;
        if (!this.fullName.equals(tableInfo.getFullName()))
            return false;
        return true;
    }

    @Override
    public int hashCode()
    {
        int result = this.tableName.hashCode();
        result = 31 * result + this.databaseName.hashCode();
        result = 31 * result + this.fullName.hashCode();
        return result;
    }

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

    @Override
    public String toString()
    {
        return databaseName + ' ' + tableName;
    }
}
