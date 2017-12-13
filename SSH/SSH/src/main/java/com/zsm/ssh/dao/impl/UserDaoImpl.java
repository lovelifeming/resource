package com.zsm.ssh.dao.impl;

import com.zsm.ssh.dao.UserDao;
import com.zsm.ssh.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

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
        Session session = getSessionFactory().openSession();
        String sql = "select id,username,password,role,status,email,regtime,regip from user";
        Query query = getSessionFactory().openSession().createQuery(sql);
        //将所有的数据查询出来并放到List集合里
        List<User> list = query.getResultList();
        session.close();

//        ArrayList<User> users = new ArrayList<>();
//        User user = new User();
//        user.setId(1);
//        user.setUsername("testName");
//        user.setPassword("daaghjk");
//        user.setRole("2");
//        user.setRegtime(new Date());
//        users.add(user);
        return list;
    }

    @Override
    public User loginCheck(String username, String pwd)
    {
        //得到此类提供的模板实现增删改查
        HibernateTemplate ht = this.getHibernateTemplate();
        //得到一个集合
        List<User> list = (List<User>)ht.find("from user where name=? and password=? ", username, pwd);

        //使用三元运算符，防止list.get(0)时报空指针。
        return list.size() > 0 ? list.get(0) : null;
    }

    public boolean Register(User user)
    {
        Session session = getSessionFactory().openSession();
        try
        {
            session.save(user);
            return true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        finally
        {
            session.close();
        }
    }

}
