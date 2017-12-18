package com.zsm.ssh.model;

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
@Table(name = "student")
public class User implements Serializable
{
    private static final long serialVersionUID = 2313421496945642L;

    private int id;

    private String user_no;

    private String user_name;

    private String password;

    private String user_sex;

    private Date user_birthday;

    private String user_class;

    private Date createtime;

    private Date updatetime;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUser_no()
    {
        return user_no;
    }

    public void setUser_no(String user_no)
    {
        this.user_no = user_no;
    }

    public String getUser_name()
    {
        return user_name;
    }

    public void setUser_name(String user_name)
    {
        this.user_name = user_name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUser_sex()
    {
        return user_sex;
    }

    public void setUser_sex(String user_sex)
    {
        this.user_sex = user_sex;
    }

    public Date getUser_birthday()
    {
        return user_birthday;
    }

    public void setUser_birthday(Date user_birthday)
    {
        this.user_birthday = user_birthday;
    }

    public String getUser_class()
    {
        return user_class;
    }

    public void setUser_class(String user_class)
    {
        this.user_class = user_class;
    }

    public Date getCreatetime()
    {
        return createtime;
    }

    public void setCreatetime(Date createtime)
    {
        this.createtime = createtime;
    }

    public Date getUpdatetime()
    {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime)
    {
        this.updatetime = updatetime;
    }

}
