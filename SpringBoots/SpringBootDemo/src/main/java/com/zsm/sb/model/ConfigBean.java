package com.zsm.sb.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/26 16:42.
 * @Modified By:
 */
@Configuration
@ConfigurationProperties(prefix = "zsm")
@PropertySource("classpath:properties/config-bean.properties")
public class ConfigBean
{
    private String name;

    private String secret;

    private int number;

    private long bignumber;

    private int tennumber;

    private int rangenumber;

    private String uuid;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSecret()
    {
        return secret;
    }

    public void setSecret(String secret)
    {
        this.secret = secret;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public long getBignumber()
    {
        return bignumber;
    }

    public void setBignumber(long bignumber)
    {
        this.bignumber = bignumber;
    }

    public int getTennumber()
    {
        return tennumber;
    }

    public void setTennumber(int tennumber)
    {
        this.tennumber = tennumber;
    }

    public int getRangenumber()
    {
        return rangenumber;
    }

    public void setRangenumber(int rangenumber)
    {
        this.rangenumber = rangenumber;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }
}
