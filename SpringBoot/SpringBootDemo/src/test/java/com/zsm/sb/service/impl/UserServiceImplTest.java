package com.zsm.sb.service.impl;

import com.zsm.sb.dao.UserDao;
import com.zsm.sb.util.AbstractSpringTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/1/2 9:41.
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest extends AbstractSpringTest
{
    @Autowired
    private UserDao userDao;

    @Ignore
    @Test
    public void testAssertThat()
    {
        //相等
        assertThat(userDao.queryUserById(108L).getUserName(), is("曾华"));
    }
}
