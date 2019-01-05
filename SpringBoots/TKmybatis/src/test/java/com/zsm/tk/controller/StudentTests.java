package com.zsm.tk.controller;

import com.zsm.tk.Application;
import com.zsm.tk.model.Student;
import com.zsm.tk.service.BaseStudentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/12/3 17:22.
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class StudentTests
{
    @Autowired
    private BaseStudentService service;

    @Test
    public void testSelectStudent()
    {
        List<Student> students = service.selectAllStudent();
        Assert.assertEquals(8,students.size());

        List<Student> student = service.selectStudentByName("王芳");
        Assert.assertEquals("查询所有名叫王芳的同学",1,student.size(),0);

        Student student1 = service.selectByPrimaryKey("1");
        Assert.assertEquals("查询第一个同学的学号","108",student1.getUserNo());
    }
}
