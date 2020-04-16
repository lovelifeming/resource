package com.zsm.service;

import com.zsm.pojo.ResultSet;
import com.zsm.pojo.UserInfo;

import java.util.List;


public interface UserService
{
    ResultSet<List<UserInfo>> getUserInfo();
}
