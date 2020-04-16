package com.zsm;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableDiscoveryClient
@EnableFeignClients
@EnableSwagger2
@EnableApolloConfig
@MapperScan("com.zsm.mapper")
@SpringBootApplication
public class JDApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(JDApplication.class, args);
    }
}
