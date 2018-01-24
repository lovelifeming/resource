package com.zsm.ssh.service.impl;

import com.zsm.ssh.dao.UserDao;
import com.zsm.ssh.model.User;
import com.zsm.ssh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/6 15:31.
 * @Modified By:
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> findAllUser()
    {
        List<User> allUser = userDao.findAllUser();
        return allUser;
    }

    @Override
    public int saveEntity(User user)
    {
        int id = userDao.saveEntity(user);
        return id;
    }

    @Override
    public boolean saveOrUpdateEntity(User user)
    {
        try
        {
            userDao.saveOrUpdateEntity(user);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateEntity(User user)
    {
        try
        {
            userDao.updateEntity(user);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getByName(String name)
    {
        try
        {
            User user = userDao.getByName(name);
            return user;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getByNo(String no)
    {
        try
        {
            User user = userDao.getByNo(no);
            return user;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteUserById(Integer id)
    {
        try
        {
            userDao.deleteUserById(id);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUserByNo(String no)
    {
        try
        {
            userDao.deleteUserByNo(no);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUserByEntity(User user)
    {
        try
        {
            userDao.deleteUserByEntity(user);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
