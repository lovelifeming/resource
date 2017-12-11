package com.zsm.ssh.service.impl;

import com.zsm.ssh.dao.UserDao;
import com.zsm.ssh.entity.User;
import com.zsm.ssh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/6 15:31.
 * @Modified By:
 */
@Service("userService")
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getAllUser()
    {
        List<User> allUser = userDao.findAllUser();
        return allUser;
    }
}
