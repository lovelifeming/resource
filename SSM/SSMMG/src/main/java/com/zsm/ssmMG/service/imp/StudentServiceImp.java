package com.zsm.ssmMG.service.imp;

import com.zsm.ssmMG.dao.Student_InfoMapper;
import com.zsm.ssmMG.model.Student_Info;
import com.zsm.ssmMG.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/3/31.
 * @Modified By:
 */
@Service
public class StudentServiceImp implements StudentService
{
    @Autowired
    private Student_InfoMapper studentInfoMapper;

    @Override
    public Student_Info selectStudentByNo(String no)
    {
        return studentInfoMapper.selectStudentByUserNo(no);
    }
}
