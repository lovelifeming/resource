package com.zsm.sb.util;

import com.zsm.sb.controller.Application;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/1/3 9:47.
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@WebAppConfiguration
@ContextConfiguration(value = {"/config/application.properties"})
public abstract class AbstractSpringTest
{

}
