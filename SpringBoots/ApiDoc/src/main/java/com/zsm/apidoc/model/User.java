package com.zsm.apidoc.model;

/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2019-05-24 01:37.
 * @Modified By:
 */
public class User
{
    private String name;

    private Integer age;

    private String sex;

    private String hobby;

    public User(String name, Integer age, String sex, String hobby)
    {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.hobby = hobby;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getAge()
    {
        return age;
    }

    public void setAge(Integer age)
    {
        this.age = age;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getHobby()
    {
        return hobby;
    }

    public void setHobby(String hobby)
    {
        this.hobby = hobby;
    }
}
