package com.zsm.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


/**
 * @Author: zeng.
 * @Date:Created in 2020-05-11 10:34.
 * @Description: 配置多源数据库
 */
@Configuration
public class DataSourceJDBCConfig
{
    //@Primary
    @Bean(name = "dataSource1")
    @ConfigurationProperties(prefix = "spring.datasource.ds1")
    public DataSource dataSource()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplate1")
    public JdbcTemplate mysqlJdbcTemplate(@Qualifier("dataSource1") DataSource mysqlDataSource)
    {
        return new JdbcTemplate(mysqlDataSource);
    }

    //@Primary
    @Bean(name = "dataSource2")
    @ConfigurationProperties(prefix = "spring.datasource.ds2")
    public DataSource dataSource1()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplate2")
    public JdbcTemplate mysqlJdbcTemplate1(@Qualifier("dataSource2") DataSource mysqlDataSource)
    {
        return new JdbcTemplate(mysqlDataSource);
    }
}
