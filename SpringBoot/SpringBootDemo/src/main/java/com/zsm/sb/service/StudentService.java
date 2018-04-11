package com.zsm.sb.service;

import com.zsm.sb.model.Student;

import java.util.List;
import java.util.Map;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/29 14:52.
 * @Modified By:
 */
public interface StudentService
{
    Student queryStudentById(Long userNo);

    int addStudent(Student user);

    int updateStudent(Student user);

    int deleteStudentByIds(String[] userNos);

    List<Student> queryStudentList(Map<String, Object> params);
}
