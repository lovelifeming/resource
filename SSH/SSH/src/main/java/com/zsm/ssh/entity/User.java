package com.zsm.ssh.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/6 15:19.
 * @Modified By:
 */
@Entity
@Table(name = "user")
public class User implements Serializable
{
    private static final long serialVersionUID = 2313421496945642L;
    private int id;

    private String username;

    private String password;

    private String role;

    private int status;

    private String email;

    private Date regtime;

    private String regip;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Date getRegtime()
    {
        return regtime;
    }

    public void setRegtime(Date regtime)
    {
        this.regtime = regtime;
    }

    public String getRegip()
    {
        return regip;
    }

    public void setRegip(String regip)
    {
        this.regip = regip;
    }

}
