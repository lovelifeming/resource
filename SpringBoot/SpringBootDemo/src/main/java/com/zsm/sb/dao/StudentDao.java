package com.zsm.sb.dao;

import com.zsm.sb.model.Student;
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
public interface StudentDao
{
    @Select("select * from student where user_no = #{user_no}")
    @Results(id = "userMap", value = {
        @Result(column = "user_no", property = "user_no", javaType = Long.class),
        @Result(property = "user_name", column = "user_name", javaType = String.class),
        @Result(property = "password", column = "password", javaType = String.class)
    })
    Student queryStudentById(@Param("user_no") Long userNo);

    int addStudent(Student user);

    int updateStudent(Student user);

    int deleteStudentByIds(String[] userNos);

    List<Student> queryStudentList(Map<String, Object> params);

}
