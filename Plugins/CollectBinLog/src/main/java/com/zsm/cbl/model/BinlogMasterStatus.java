package com.zsm.cbl.model;

/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/9.
 * @Modified By:
 */
public class BinlogMasterStatus
{
    private String binlogName;
    private long position;

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
}
