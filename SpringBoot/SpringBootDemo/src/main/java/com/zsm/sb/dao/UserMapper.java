package com.zsm.sb.dao;

import com.zsm.sb.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface UserMapper
{
    @Select("SELECT * from student where user_name=#{username}")
    User findUserByName(@Param("username") String userName);

    @Update("update student set password=#{pwd} where user_no=#{userno}")
    int resetPassword(@Param("pwd") String pwd, @Param("userno") String userNo);
}
