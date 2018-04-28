package com.zsm.sb;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.MultipartConfigElement;


/**
 * @SpringBootApplicatoin=@Configuration+@EnableAutoConfiguration+@ComponentScan
 * @SpringBootApplicatoin是用的@ComponentScan扫描的，扫描的是Component，包括@Component, @Controller, @Service, @Repository等
 * Mybatis自动扫描配置中，使用注解配置时，我们只要在@MapperScan中配置我们需要扫描的Mapper位置
 */
@SpringBootApplication
@MapperScan(value = "com.zsm.sb.dao")
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
/**
 * 通过 @PropertySource 注解，并定义多个路径，后面的属性将覆盖前面的。
 * 在需要覆盖属性时，只需在 jar 同级目录中放入 config 文件夹并在其中放入 config.properties 文件即可。
 * ignoreResourceNotFound = true，忽略找不到时的错误提示，否则开发环境编译不过，因为找不到这个路径下的文件
 * 第一种是     在jar包的同一目录下建一个config文件夹，然后把配置文件放到这个文件夹下；
 * 第二种是     直接把配置文件放到jar包的同级目录；
 * 第三种是     在classpath下建一个config文件夹，然后把配置文件放进去；
 * 第四种是     在classpath下直接放配置文件。
 */
@PropertySource(value = {"classpath:/application.properties", "file:/application.properties",
    "file:/config/config.properties"}, ignoreResourceNotFound = true)
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

    /**
     * 文件上传配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement()
    {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize("10240KB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("102400KB");
        return factory.createMultipartConfig();
    }
}
