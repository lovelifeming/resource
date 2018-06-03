package com.zsm.canal.model;

/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/5/18.
 * @Modified By:
 */
public class Cluster
{
    private String zkservice;

    private String destination;

    private String topicname;

    private String username;

    private String password;

    public String getZkservice()
    {
        return zkservice;
    }

    public void setZkservice(String zkservice)
    {
        this.zkservice = zkservice;
    }

    public String getDestination()
    {
        return destination;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    public String getTopicname()
    {
        return topicname;
    }

    public void setTopicname(String topicname)
    {
        this.topicname = topicname;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "Cluster{" +
               "zkservice='" + zkservice + '\'' +
               ", destination='" + destination + '\'' +
               ", topicname='" + topicname + '\'' +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
