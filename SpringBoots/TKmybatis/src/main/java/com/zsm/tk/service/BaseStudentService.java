package com.zsm.tk.service;

import com.zsm.tk.model.Student;

import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/12/3 17:13.
 * @Modified By:
 */
public interface BaseStudentService
{
    Student selectByPrimaryKey(String id);

    List<Student> selectStudentByName(String name);

    List<Student> selectAllStudent();
}
