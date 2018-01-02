package com.zsm.sb.service.impl;

import com.zsm.sb.dao.UserDao;
import com.zsm.sb.model.User;
import com.zsm.sb.service.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/29 14:52.
 * @Modified By:
 */
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public User queryUserById(Long userNo)
    {
        return null;
    }

    @Override
    public int addUser(User user)
    {
        return 0;
    }

    @Override
    public int updateUser(User user)
    {
        return 0;
    }

    @Override
    public int deleteUserByIds(String[] userNos)
    {
        return 0;
    }

    @Override
    public List<User> queryUserList(Map<String, Object> params)
    {
        //分页查询
        PageHelper.startPage(Integer.parseInt(params.get("page").toString()),
            Integer.parseInt(params.get("rows").toString()));
        return userDao.queryUserList(params);
    }
}
