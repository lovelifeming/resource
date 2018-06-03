package com.zsm.canal.model;

/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/5/18.
 * @Modified By:
 */
public class Simple
{
    /**
     * instance 实例名称
     */
    private String destination;

    /**
     * kafka集群topic名称
     */
    private String topicname;

    /**
     * canal服务器IP地址
     */
    private String hostname;

    /**
     * canal服务器端口
     */
    private Integer port;

    /**
     * canal服务器用户名
     */
    private String username;

    /**
     * canal服务器密码
     */
    private String password;

    /**
     * canal服务器zookeeper集群的节点IP
     */
    private String zkurl;

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

    public String getHostname()
    {
        return hostname;
    }

    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
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

    public String getZkurl()
    {
        return zkurl;
    }

    public void setZkurl(String zkurl)
    {
        this.zkurl = zkurl;
    }

    @Override
    public String toString()
    {
        return "Simple{" +
               "destination='" + destination + '\'' +
               ", topicname='" + topicname + '\'' +
               ", hostname='" + hostname + '\'' +
               ", port=" + port +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", zkurl='" + zkurl + '\'' +
               '}';
    }
}
