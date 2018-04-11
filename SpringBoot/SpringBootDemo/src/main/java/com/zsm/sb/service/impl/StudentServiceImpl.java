package com.zsm.sb.service.impl;

import com.github.pagehelper.PageHelper;
import com.zsm.sb.dao.StudentDao;
import com.zsm.sb.model.Student;
import com.zsm.sb.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/29 14:52.
 * @Modified By:
 */
public class StudentServiceImpl implements StudentService
{
    @Autowired
    private StudentDao studentDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Student queryStudentById(Long userNo)
    {
        return null;
    }

    @Override
    public int addStudent(Student user)
    {
        return 0;
    }

    @Override
    public int updateStudent(Student user)
    {
        return 0;
    }

    @Override
    public int deleteStudentByIds(String[] userNos)
    {
        return 0;
    }

    @Override
    public List<Student> queryStudentList(Map<String, Object> params)
    {
        //分页查询
        PageHelper.startPage(Integer.parseInt(params.get("page").toString()),
            Integer.parseInt(params.get("rows").toString()));
        return studentDao.queryStudentList(params);
    }
}
