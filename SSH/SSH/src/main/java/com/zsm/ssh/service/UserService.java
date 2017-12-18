package com.zsm.ssh.service;

import com.zsm.ssh.model.User;

import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/6 15:30.
 * @Modified By:
 */
public interface UserService
{
    List<User> findAllUser();

    int saveEntity(User user);

    boolean saveOrUpdateEntity(User user);

    boolean updateEntity(User user);

    User getByName(String name);

    User getByNo(String no);

    boolean deleteUserById(Integer id);

    boolean deleteUserByNo(String no);

    boolean deleteUserByEntity(User user);
}
