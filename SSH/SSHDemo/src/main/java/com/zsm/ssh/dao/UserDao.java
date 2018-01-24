package com.zsm.ssh.dao;

import com.zsm.ssh.model.User;

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

    int saveEntity(User user);

    void saveOrUpdateEntity(User user);

    void updateEntity(User user);

    User getByName(String name);

    User getByNo(String no);

    void deleteUserById(Integer id);

    void deleteUserByNo(String no);

    void deleteUserByEntity(User user);
}
