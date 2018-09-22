package com.zsm.sb.service.impl;

import com.zsm.sb.dao.StudentMapper;
import com.zsm.sb.model.Student;
import com.zsm.sb.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 事务的配置，就是在mybatis的基础上加上两个注解
 * 需要的注解为@EnableTransactionManagement 和@Transactional 两个
 * spring boot启动类上添加@EnableTransactionManagement开启事务
 * 在服务层需要添加的函数上添加@Transactional注解或者@Transactional(propagation = Propagation.REQUIRED,
 * isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
 *
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/10.
 * @Modified By:
 */
@Component
//@Transactional
public class StudentServiceImpl implements StudentService
{
    @Autowired
    private StudentMapper studentMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT,
        timeout = 36000, rollbackFor = Exception.class)
    public Student selectStudentByName(String name)
    {
        return studentMapper.selectStudentByName(name);
    }
}
