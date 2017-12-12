package com.zsm.ssh.dao.impl;

import com.zsm.ssh.dao.UserDao;
import com.zsm.ssh.entity.User;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/6 15:36.
 * @Modified By:
 */
@Repository("userDao")
public class UserDaoImpl extends HibernateDaoSupport implements UserDao
{

    @Override
    public List<User> findAllUser()
    {
        //获取session
//        Session session = getSessionFactory().openSession();
//        Query query = session.createQuery("from user");
//        //将所有的数据查询出来并放到List集合里
//        List<User> list = query.getResultList();
//        session.close();
//        getSessionFactory().close();

        ArrayList<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1);
        user.setUsername("testName");
        user.setPassword("daaghjk");
        user.setRole("2");
        user.setRegtime(new Date());
        users.add(user);

        return users;
    }
}
