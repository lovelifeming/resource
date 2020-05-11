package com.zsm.sb.dao;

import com.zsm.sb.model.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface StudentMapper
{
    int deleteByPrimaryKey(@Param("id") Integer id, @Param("user_no") String user_no);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(@Param("id") Integer id, @Param("user_no") String user_no);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);

    Student selectStudentByName(String name);

    List<Student> getUserInfoList(String name);
}
