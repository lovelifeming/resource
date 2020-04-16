package com.zsm.service.impl;

import com.zsm.mapper.UserInfoMapper;
import com.zsm.pojo.ResultSet;
import com.zsm.pojo.UserInfo;
import com.zsm.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class UserInfoServiceImpl implements UserService
{
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public ResultSet<List<UserInfo>> getUserInfo()
    {
        List<UserInfo> userInfo = userInfoMapper.getUserInfo();
        return ResultSet.success(userInfo);
    }
}
