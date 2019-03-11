package com.zsm.sb.service.impl;

import com.zsm.sb.model.Student;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/10.
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class StudentServiceImpTest
{
    @Autowired
    private StudentServiceImpl studentServiceImp;

    @Test
    public void selectByName()
    {
        Student student = studentServiceImp.selectStudentByName("陆君");

        System.out.println(student.toString());
        Assert.assertEquals("103", student.getUser_no());
    }
}
