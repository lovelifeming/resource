package com.zsm.ssh.dao;

import com.zsm.ssh.entity.User;

import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/6 15:32.
 * @Modified By:
 */
public interface UserDao
{
    List<User> findAllUser();
}
