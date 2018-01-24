package com.zsm.ssh.dao.impl;

import com.zsm.ssh.dao.UserDao;
import com.zsm.ssh.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


/**
 * @Author: zsm.
 * @Description:
 * @Date:Created in 2017/12/16 22:50.
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/struts/applicationContext.xml"})
public class UserDaoImplTest
{
    @Autowired
    private UserDao userDao;

    @Test
    public void findAllUser() throws Exception
    {
        List<User> users = userDao.findAllUser();
        Assert.assertTrue(users.size() > 8);
    }

    @Test
    public void saveEntity() throws Exception
    {
        User user = getUser();
        Integer id = userDao.saveEntity(user);
        Assert.assertTrue(id > 8);
    }

    @Test
    public void saveOrUpdateEntity() throws Exception
    {
        User user = getUser();
        user.setId(10);
        userDao.saveOrUpdateEntity(user);
    }

    @Test
    public void updateEntity() throws Exception
    {
        User user = getUser();
        user.setId(10);
        userDao.saveEntity(user);
    }

    @Test
    public void getByName() throws Exception
    {
        User user = userDao.getByName("杜玉萍");
        Assert.assertEquals("113", user.getUser_no());
    }

    @Test
    public void getByNo() throws Exception
    {
        User user = userDao.getByNo("114");
        Assert.assertEquals("李元芳", user.getUser_name());
    }

    @Test
    public void deleteUserById() throws Exception
    {
        userDao.deleteUserById(10);
    }

    @Test
    public void deleteUserByNo() throws Exception
    {
        userDao.deleteUserByNo("114");
    }

    @Test
    public void deleteUserByEntity() throws Exception
    {
        User user=getUser();
        user.setId(10);
        userDao.deleteUserByEntity(user);
    }

    private User getUser() throws ParseException
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User user = new User();
        user.setUser_name("李元芳");
        user.setUser_no("114");
        user.setPassword("efafa13");
        user.setUser_sex("男");
        Date bir = formatter.parse("1990-06-03 00:00:00");
        user.setUser_birthday(bir);
        user.setUser_class("95035");
        Date cur = new Date();
        user.setCreatetime(cur);
        cur.setDate(-1);
        user.setUpdatetime(cur);
        return user;
    }
}