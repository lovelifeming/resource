package com.zsm.sb.service;

import com.zsm.sb.model.User;

import java.util.List;
import java.util.Map;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/29 14:52.
 * @Modified By:
 */
public interface UserService
{
    User queryUserById(Long userNo);

    int addUser(User user);

    int updateUser(User user);

    int deleteUserByIds(String[] userNos);

    List<User> queryUserList(Map<String, Object> params);
}
