package com.zsm.ssh.dao.impl;

import com.zsm.ssh.dao.UserDao;
import com.zsm.ssh.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    //这里的属性名一定要和配置中的属性名一致
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<User> findAllUser()
    {
        Session session = sessionFactory.openSession();
        //将所有的数据查询出来并放到List集合里 User是表对应的实例名称
        List<User> list = session.createQuery("from User").list();
        session.close();
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int saveEntity(User user)
    {
        Session session = sessionFactory.getCurrentSession();
        //返回插入数据id
        Object id = session.save(user);
        return Integer.valueOf(id.toString());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdateEntity(User user)
    {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateEntity(User user)
    {
        Session session = sessionFactory.getCurrentSession();
        session.update(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User getByName(String name)
    {
        Session session = sessionFactory.getCurrentSession();
        //根据id查询,结果返回 User.class
        User user = (User)session.get(User.class, 2);
        //将所有的数据查询出来并放到List集合里 User是表对应的实例名称
        List users = session.createQuery("from User where user_name='" + name + "'").list();
        return (User)users.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User getByNo(String no)
    {
        Session session = sessionFactory.getCurrentSession();
        List users = session.createQuery("from User where user_no='" + no + "'").list();
        return (User)users.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserById(Integer id)
    {
        Session session = sessionFactory.getCurrentSession();
        String sql = "delete from User where id=:id";
        Query query = session.createQuery(sql);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserByNo(String no)
    {
        Session session = sessionFactory.getCurrentSession();
        String sql = "delete from User where user_no=:no";
        Query query = session.createQuery(sql);
        query.setParameter("no", no);
        query.executeUpdate();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserByEntity(User user)
    {
        Session session = sessionFactory.getCurrentSession();
        //传入实例对象，比较id删除对应行，，没有id匹配就不删除
        session.delete(User.class.getName(), user);
    }
}
