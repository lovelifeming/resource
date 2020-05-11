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
 * @Description: 配置多源数据库 db2
 */
@Configuration
@MapperScan(basePackages = {"com.zsm.mapper.db2"}, sqlSessionFactoryRef = "db2SqlSessionFactory")
public class DataSource2Config
{
    @Bean(name = "db2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ds2")
    public DataSource setDataSource()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "db2Configuration")
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration globalConfiguration()
    {
        return new org.apache.ibatis.session.Configuration();
    }

    @Bean(name = "db2SqlSessionFactory")
    public SqlSessionFactory setSqlSessionFactory(@Qualifier("db2DataSource") DataSource dataSource,
                                                  @Qualifier("db2Configuration") org.apache.ibatis.session.Configuration configuration)
        throws Exception
    {
        String url = "classpath:com/zsm/mapper/db2/*.xml";
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

    @Bean(name = "db2SqlSessionTemplate")
    public SqlSessionTemplate setSqlSessionTemplate(
        @Qualifier("db2SqlSessionFactory") SqlSessionFactory sqlSessionFactory)
        throws Exception
    {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    // 创建事务管理器
    @Primary
    @Bean(name = "db2Manager")
    public PlatformTransactionManager txManager(@Qualifier("db2DataSource") DataSource dataSource)
    {
        return new DataSourceTransactionManager(dataSource);
    }
}
