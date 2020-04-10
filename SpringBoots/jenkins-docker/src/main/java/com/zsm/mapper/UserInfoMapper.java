package com.zsm.mapper;

import com.zsm.pojo.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserInfoMapper
{
    List<UserInfo> getUserInfo();
}
