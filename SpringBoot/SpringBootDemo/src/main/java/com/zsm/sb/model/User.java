package com.zsm.sb.model;

import java.util.Date;
import javax.persistence.*;


@Table(name = "student")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Id
    @Column(name = "user_no")
    private String userNo;

    @Column(name = "user_name")
    private String userName;

    private String password;

    @Column(name = "user_sex")
    private String userSex;

    @Column(name = "user_birthday")
    private Date userBirthday;

    @Column(name = "user_class")
    private String userClass;

    @Column(name = "createtime")
    private Date createtime;

    private Date updatetime;

    /**
     * @return id
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return user_no
     */
    public String getUserNo()
    {
        return userNo;
    }

    /**
     * @param userNo
     */
    public void setUserNo(String userNo)
    {
        this.userNo = userNo == null ? null : userNo.trim();
    }

    /**
     * @return user_name
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName)
    {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * @return password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password)
    {
        this.password = password == null ? null : password.trim();
    }

    /**
     * @return user_sex
     */
    public String getUserSex()
    {
        return userSex;
    }

    /**
     * @param userSex
     */
    public void setUserSex(String userSex)
    {
        this.userSex = userSex == null ? null : userSex.trim();
    }

    /**
     * @return user_birthday
     */
    public Date getUserBirthday()
    {
        return userBirthday;
    }

    /**
     * @param userBirthday
     */
    public void setUserBirthday(Date userBirthday)
    {
        this.userBirthday = userBirthday;
    }

    /**
     * @return user_class
     */
    public String getUserClass()
    {
        return userClass;
    }

    /**
     * @param userClass
     */
    public void setUserClass(String userClass)
    {
        this.userClass = userClass == null ? null : userClass.trim();
    }

    /**
     * @return createtime
     */
    public Date getCreatetime()
    {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(Date createtime)
    {
        this.createtime = createtime;
    }

    /**
     * @return updatetime
     */
    public Date getUpdatetime()
    {
        return updatetime;
    }

    /**
     * @param updatetime
     */
    public void setUpdatetime(Date updatetime)
    {
        this.updatetime = updatetime;
    }
}
