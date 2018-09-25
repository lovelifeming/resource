package com.zsm.sb.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zsm.sb.sensitive.SensitiveInfo;
import com.zsm.sb.sensitive.SensitiveType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;


@XmlAccessorType(XmlAccessType.FIELD)
//使用原始字段(大小写，名称)，禁用自动大小写驼峰命令
//JsonAutoDetect.Visibility.ANY : 表示所有字段都可以被发现, 包括private修饰的字段, 解决大小写问题
//JsonAutoDetect.Visibility.NONE : 表示get方法不可见,解决字段重复问题
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Student
{
    @XmlElement(name = "序号")
    private Integer id;

    @XmlElement(name = "编号")
    private String user_no;

    @XmlElement(name = "名称")
    private String user_name;

    @SensitiveInfo(value = SensitiveType.PASSWORD)
    @XmlElement(name = "密码")
    private String password;

    @XmlElement(name = "性别")
    private String user_sex;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @XmlElement(name = "生日")
    private Date user_birthday;

    @XmlElement(name = "班级")
    private String user_class;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlElement(name = "创建时间")
    private Date createtime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlElement(name = "更新时间")
    private Date updatetime;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUser_no()
    {
        return user_no;
    }

    public void setUser_no(String user_no)
    {
        this.user_no = user_no == null ? null : user_no.trim();
    }

    public String getUser_name()
    {
        return user_name;
    }

    public void setUser_name(String user_name)
    {
        this.user_name = user_name == null ? null : user_name.trim();
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password == null ? null : password.trim();
    }

    public String getUser_sex()
    {
        return user_sex;
    }

    public void setUser_sex(String user_sex)
    {
        this.user_sex = user_sex == null ? null : user_sex.trim();
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
        this.user_class = user_class == null ? null : user_class.trim();
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

    @Override
    public String toString()
    {
        return "Student{" +
               "id=" + id +
               ", user_no='" + user_no + '\'' +
               ", user_name='" + user_name + '\'' +
               ", password='" + password + '\'' +
               ", user_sex='" + user_sex + '\'' +
               ", user_birthday=" + user_birthday +
               ", user_class='" + user_class + '\'' +
               ", createtime=" + createtime +
               ", updatetime=" + updatetime +
               '}';
    }
}
