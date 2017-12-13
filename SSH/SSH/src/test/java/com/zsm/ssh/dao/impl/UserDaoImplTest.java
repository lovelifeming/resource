package com.zsm.ssh.dao.impl;

import com.zsm.ssh.entity.User;
import com.zsm.ssh.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/13 10:39.
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/struts/applicationContext.xml", "classpath:/struts/hibernate.cfg.xml"})
public class UserDaoImplTest
{
    @Test
    public void findAllUser()
        throws Exception
    {
        //通过spring.xml配置文件创建Spring的应用程序上下文环境
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/struts/applicationContext.xml");
        //从Spring的IOC容器中获取bean对象
        UserDaoImpl userService = (UserDaoImpl) ac.getBean("userDao");
        //执行测试方法
        List<User> users = userService.findAllUser();
        Assert.assertEquals(users.size(), "2");
    }

}
