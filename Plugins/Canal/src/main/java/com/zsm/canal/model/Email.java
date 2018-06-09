package com.zsm.canal.model;

import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/6/4.
 * @Modified By:
 */
public class Email
{
    private String from;

    private String password;

    private List<String> sendto;

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public List<String> getSendto()
    {
        return sendto;
    }

    public void setSendto(List<String> sendto)
    {
        this.sendto = sendto;
    }

    @Override
    public String toString()
    {
        return "Email{" +
               "from='" + from + '\'' +
               ", password='" + password + '\'' +
               ", email=" + sendto +
               '}';
    }
}
