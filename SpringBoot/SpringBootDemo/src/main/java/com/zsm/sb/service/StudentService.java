package com.zsm.sb.service;

import com.zsm.sb.model.Student;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/10.
 * @Modified By:
 */

public interface StudentService
{
    Student selectStudentByName(String name);
}
