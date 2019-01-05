package com.zsm.tk.service.impl;

import com.zsm.tk.dao.StudentDao;
import com.zsm.tk.model.Student;
import com.zsm.tk.service.BaseStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/12/3 17:15.
 * @Modified By:
 */
@Service
@Transactional
public class BaseStudentServiceImpl implements BaseStudentService
{
    @Autowired
    private StudentDao mapper;

    @Override
    public Student selectByPrimaryKey(String id)
    {
        Student student = mapper.selectByPrimaryKey(id);
        return student;
    }

    @Override
    public List<Student> selectStudentByName(String name)
    {
        Student student = new Student();
        student.setUserName(name);
        List<Student> students = mapper.select(student);
        return students;
    }

    @Override
    public List<Student> selectAllStudent()
    {
        List<Student> students = mapper.selectAll();
        return students;
    }
}
