package com.zsm.config;

import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;


/**
 * @Author: zeng.
 * @Date:Created in 2020-05-11 10:15.
 * @Description:
 */
@Configuration
@MapperScan(basePackages = {"com.zsm.mapper.db1"}, sqlSessionFactoryRef = "db1SqlSessionFactory")
public class DataSource1Config
{
    @Bean(name = "db1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ds1")
    public DataSource setDataSource()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "db1Configuration")
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration globalConfiguration()
    {
        return new org.apache.ibatis.session.Configuration();
    }

    @Bean(name = "db1SqlSessionFactory")
    public SqlSessionFactory setSqlSessionFactory(@Qualifier("db1DataSource") DataSource dataSource,
                                                  @Qualifier("db1Configuration") org.apache.ibatis.session.Configuration configuration)
        throws Exception
    {
        String url = "classpath:com/zsm/mapper/db1/*.xml";
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfiguration(configuration);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(url));
        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
        config.setMapUnderscoreToCamelCase(true); // 开启驼峰命名支持
        bean.setConfiguration(config);
        //格式化sql语句打印
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        Properties properties = new Properties();
        properties.setProperty("format", "true");
        performanceInterceptor.setProperties(properties);
        bean.setPlugins(new Interceptor[] {performanceInterceptor});
        return bean.getObject();
    }

    @Bean(name = "db1SqlSessionTemplate")
    public SqlSessionTemplate setSqlSessionTemplate(
        @Qualifier("db1SqlSessionFactory") SqlSessionFactory sqlSessionFactory)
        throws Exception
    {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    // 创建事务管理器
    @Primary
    @Bean(name = "db1Manager")
    public PlatformTransactionManager txManager(@Qualifier("db1DataSource") DataSource dataSource)
    {
        return new DataSourceTransactionManager(dataSource);
    }
}
