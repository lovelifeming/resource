package com.zsm.ssmMG.service.imp;

import com.zsm.ssmMG.model.Student_Info;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/3/31.
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/applicationContext.xml")
public class StudentServiceImpTest
{
    @Autowired
    private StudentServiceImp studentServiceImp;

    @Test
    public void selectStudentByNo()
        throws Exception
    {
        Student_Info student_info = studentServiceImp.selectStudentByNo("103");
        System.out.println(student_info.getUser_name());
        Assert.assertEquals("陆君", student_info.getUser_name());
    }

}
