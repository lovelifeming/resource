package com.zsm.sb.service.imp;

import com.zsm.sb.dao.StudentMapper;
import com.zsm.sb.model.Student;
import com.zsm.sb.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/10.
 * @Modified By:
 */
@Component
public class StudentServiceImp implements StudentService
{
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Student selectStudentByName(String name)
    {
        return studentMapper.selectStudentByName(name);
    }
}
