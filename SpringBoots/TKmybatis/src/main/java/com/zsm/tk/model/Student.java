package com.zsm.tk.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * TkMybatis默认使用继承Mapper接口中传入的实体类对象去数据库寻找对应的表,因此如果表名与实体类名
 * 不满足对应规则时,会报错,这时使用@Table为实体类指定表
 *
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/12/3 16:17.
 * @Modified By:
 */
//@Table指定该实体类对应的表名,如表名为student,这里类名为Student可以不需要此注解。
@Table(name = "student")
public class Student
{
    //Id表示该字段对应数据库表的主键id.
    //@GeneratedValue中strategy表示使用数据库自带的主键生成策略.
    //@GeneratedValue中generator配置为"JDBC",在数据插入完毕之后,会自动将主键id填充到实体类中.类似普通mapper.xml中配置的selectKey标签
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    private Long id;

    private String userNo;

    private String userName;

    private String password;

    private String userSex;

    private String userBirthday;

    private String userClass;

    private String createtime;

    private String updatetime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUserNo()
    {
        return userNo;
    }

    public void setUserNo(String userNo)
    {
        this.userNo = userNo;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUserSex()
    {
        return userSex;
    }

    public void setUserSex(String userSex)
    {
        this.userSex = userSex;
    }

    public String getUserBirthday()
    {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday)
    {
        this.userBirthday = userBirthday;
    }

    public String getUserClass()
    {
        return userClass;
    }

    public void setUserClass(String userClass)
    {
        this.userClass = userClass;
    }

    public String getCreatetime()
    {
        return createtime;
    }

    public void setCreatetime(String createtime)
    {
        this.createtime = createtime;
    }

    public String getUpdatetime()
    {
        return updatetime;
    }

    public void setUpdatetime(String updatetime)
    {
        this.updatetime = updatetime;
    }

    @Override
    public String toString()
    {
        return "Student{" +
               "id=" + id +
               ", userNo='" + userNo + '\'' +
               ", userName='" + userName + '\'' +
               ", password='" + password + '\'' +
               ", userSex='" + userSex + '\'' +
               ", userBirthday='" + userBirthday + '\'' +
               ", userClass='" + userClass + '\'' +
               ", createtime='" + createtime + '\'' +
               ", updatetime='" + updatetime + '\'' +
               '}';
    }
}
