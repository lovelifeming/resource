package com.zsm.sb;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;


/**
 * @SpringBootApplicatoin=@Configuration+@EnableAutoConfiguration+@ComponentScan
 * @SpringBootApplicatoin是用的@ComponentScan扫描的，扫描的是Component，包括@Component, @Controller, @Service, @Repository等
 * Mybatis自动扫描配置中，使用注解配置时，我们只要在@MapperScan中配置我们需要扫描的Mapper位置
 */
@SpringBootApplication
@MapperScan(value = "com.zsm.sb.dao")
public class Application
{
    @Autowired
    private Environment environment;

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    //destroy-method="close"的作用是当数据库连接不使用的时候,就把该连接重新放到数据池中,方便下次使用调用.
    @Bean(initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource()
    {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        //用户名
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        //密码
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        //初始化时建立物理连接的个数
        dataSource.setInitialSize(2);
        //最大连接池数量
        dataSource.setMaxActive(20);
        //最小连接池数量
        dataSource.setMinIdle(0);
        //获取连接时最大等待时间，单位毫秒。
        dataSource.setMaxWait(60000);
        //用来检测连接是否有效的sql
        dataSource.setValidationQuery("SELECT 1");
        //申请连接时执行validationQuery检测连接是否有效
        dataSource.setTestOnBorrow(false);
        //建议配置为true，不影响性能，并且保证安全性。
        dataSource.setTestWhileIdle(true);
        //是否缓存preparedStatement，也就是PSCache
        dataSource.setPoolPreparedStatements(false);
        return dataSource;
    }
}
