package com.zsm.tk.mapper;

import com.zsm.tk.pojo.Student;
import org.apache.ibatis.annotations.Param;

public interface StudentMapper {
    int deleteByPrimaryKey(@Param("id") Integer id, @Param("userNo") String userNo);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(@Param("id") Integer id, @Param("userNo") String userNo);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);
}