package com.zsm.ssh.service.impl;

import com.zsm.ssh.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/7 13:47.
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/struts/applicationContext.xml"})
public class UserServiceImplTest
{
    @Test
    public void getAllUser()
        throws Exception
    {
        UserServiceImpl userService = new UserServiceImpl();
        List<User> users = userService.getAllUser();
        Assert.assertEquals(users.size(), "2");
    }

    @Test
    public void test() {
        // 获取配置信息
        Configuration configuration = new Configuration().configure();
        // 得到Session工厂
        SessionFactory factory = configuration.buildSessionFactory();
        // 得到Session
        Session session = factory.openSession();
        Session session1 = factory.openSession();
        // 开启事物
        Transaction transaction = session.beginTransaction();
        // 根据主键得到对象(查询单个记录)
        User st = session.get(User.class, 1);
        System.out.println(st);
        // 二级缓存
        List<User> ls = session1.createQuery("from User").setCacheable(true).list();
        for (User s : ls) {
            System.out.println(s);
        }
        // 提交事物
        // transaction.commit();
        // 关闭seeion
        session.close();
        // 关闭SessionFactory
        factory.close();
    }
}
