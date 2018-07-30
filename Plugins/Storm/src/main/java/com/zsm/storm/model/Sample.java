package com.zsm.storm.model;

import java.io.Serializable;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/7/24.
 * @Modified By:
 */
public class Sample implements Serializable
{
    private static final long serialVersionUID = -205552189912231672L;

    private String id;

    private String result;

    private String compare;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getCompare()
    {
        return compare;
    }

    public void setCompare(String compare)
    {
        this.compare = compare;
    }

    @Override
    public String toString()
    {
        return "Sample{" +
               "id='" + id + '\'' +
               ", result='" + result + '\'' +
               ", compare='" + compare + '\'' +
               '}';
    }
}
