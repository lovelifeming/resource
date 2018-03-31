package com.zsm.ssmMG.dao;

import com.zsm.ssmMG.model.Student_Info;
import com.zsm.ssmMG.model.Student_InfoExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Student_InfoMapper
{

    Student_Info selectStudentByUserNo(String no);

    long countByExample(Student_InfoExample example);

    int deleteByExample(Student_InfoExample example);

    int insert(Student_Info record);

    int insertSelective(Student_Info record);

    List<Student_Info> selectByExample(Student_InfoExample example);

    int updateByExampleSelective(@Param("record") Student_Info record, @Param("example") Student_InfoExample example);

    int updateByExample(@Param("record") Student_Info record, @Param("example") Student_InfoExample example);
}
