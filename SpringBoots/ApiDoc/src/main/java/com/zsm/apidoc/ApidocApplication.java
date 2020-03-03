package com.zsm.apidoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @ComponentScan 自定义扫描包
 */
//@ComponentScan(basePackages = {"springfox.documentation.spring.web","com.zsm.apidoc"})
@SpringBootApplication
@EnableSwagger2
public class ApidocApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ApidocApplication.class, args);
    }
}
