package com.zsm.sb.dao;

import com.zsm.sb.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/29 14:52.
 * @Modified By:
 */
@Component
@Mapper
public interface UserDao
{
    @Select("select * from student where user_no = #{user_no}")
    @Results(id = "userMap", value = {
        @Result(column = "user_no", property = "user_no", javaType = Long.class),
        @Result(property = "user_name", column = "user_name", javaType = String.class),
        @Result(property = "password", column = "password", javaType = String.class)
    })
    User queryUserById(@Param("user_no") Long userNo);

    int addUser(User user);

    int updateUser(User user);

    int deleteUserByIds(String[] userNos);

    List<User> queryUserList(Map<String, Object> params);

}
