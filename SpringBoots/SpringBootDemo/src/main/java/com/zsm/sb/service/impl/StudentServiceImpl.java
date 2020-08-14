package com.zsm.sb.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsm.sb.dao.StudentMapper;
import com.zsm.sb.model.ResultVO;
import com.zsm.sb.model.Student;
import com.zsm.sb.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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

    @Override
    public ResultVO getUserInfoList(String name, Integer pageNum, Integer pageSize)
    {
        // 开启分页插件,必须紧邻查询语句中间插入其他语句会导致失效,帮助第一个查询语句生成分页语句
        PageHelper.startPage(pageNum, pageSize);
        //底层实现原理采用AOP技术改写语句,将下面的方法中的sql语句获取到然后做个拼接 limit、套用 count
        List<Student> list = studentMapper.getUserInfoList(name);
        // 封装分页之后的数据返回给客户端展示, PageInfo封装作为一个类,所有分页属性都可以从pageInfo获取
        PageInfo<Student> pageInfo = new PageInfo<>(list);
        return ResultVO.succes(pageInfo);
    }
}
