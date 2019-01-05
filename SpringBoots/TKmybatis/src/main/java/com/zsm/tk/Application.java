package com.zsm.tk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@MapperScan("com.zsm.tk.dao")
public class Application //extends SpringBootServletInitializer
{
    private static Logger LOFFER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    // 当SpringBoot项目打成war包发布时,需要继承SpringBootServletInitializer接口实现该方法
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
//    {
//        return builder.sources(Application.class);
//    }
}
