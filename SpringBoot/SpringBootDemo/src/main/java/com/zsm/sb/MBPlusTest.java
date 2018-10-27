package com.zsm.sb;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/10/25.
 * @Modified By:
 */
public class MBPlusTest
{
    public static void main(String[] args)
    {
        AutoGenerator ag = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        //gc.setOutputDir("D:\\workspace\\SpringBoot\\src\\main\\java");//将代码生成在项目中
        gc.setOutputDir("D:\\SpringBoot");//将代码生成在指定目录中
        gc.setFileOverride(true);
        gc.setActiveRecord(false);  // 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);   // XML 二级缓存
        gc.setBaseResultMap(true);  // XML ResultMap
        gc.setBaseColumnList(false);// XML columnList
        gc.setAuthor("zsm");        // 作者
        gc.setDateType(DateType.ONLY_DATE);//只生成java.util.Date类型时间格式

        // 自定义文件命名，注意 %s 会自动填充表实体属性
        gc.setControllerName("%sController");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        ag.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/test_db?characterEncoding=utf-8");
        ag.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setTablePrefix(new String[] {"tb_"});       // 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);  // 表名生成策略
        strategy.setInclude(new String[] {"tb_user", "tb_order"}); // 需要生成的表

        strategy.setSuperServiceClass(null);
        strategy.setSuperServiceImplClass(null);
        strategy.setSuperMapperClass(null);
        ag.setStrategy(strategy);

        // 生成代码包名配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.zsm.sb");
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("dao");
        pc.setEntity("model");
        pc.setXml("mapper");
        ag.setPackageInfo(pc);

        // 执行生成
        ag.execute();
    }
}
