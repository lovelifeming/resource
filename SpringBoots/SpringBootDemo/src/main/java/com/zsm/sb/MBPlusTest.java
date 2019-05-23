package com.zsm.sb;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;


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
        gc.setOutputDir(System.getProperty("user.dir") + "/src/main/java");//将代码生成在指定目录中
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
        //设置数据库类型
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/test_db?characterEncoding=utf-8");
        ag.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);   // 全局大写命名 ORACLE 注意
        strategy.setTablePrefix(new String[] {"tb_"});       // 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);  // 表名生成策略
        strategy.setInclude(new String[] {"tb_user", "tb_order"}); // 需要生成的表
        // strategy.setExclude(new String[]{"tb_test"});   // 排除生成的表

        strategy.setSuperServiceClass(null);        // 自定义 service 父类
        strategy.setSuperServiceImplClass(null);
        strategy.setSuperMapperClass(null);
        // 自定义实体父类
        //strategy.setSuperEntityClass(com.zsm.sb.entity.BaseEntity);
        // 自定义实体，公共字段
        //strategy.setSuperEntityColumns(new String[] { "test_id", "userId" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.zsm.sb.mapper.BaseMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.zsm.sb.service.BaseService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.zsm.sb.service.BaseServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.zsm.sb.controller.BaseController");
        // 实体是否生成字段常量（默认 false）
        // public static final String ID = "userId";
        // strategy.setEntityColumnConstant(true);
        // 实体是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        //strategy.setEntityBuilderModel(true);
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

        // 自定义 xxList.jsp 生成
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        // focList.add(new FileOutConfig("/template/list.jsp.vm") {
        // @Override
        // public String outputFile(TableInfo tableInfo) {
        // // 自定义输入文件名称
        // return "D://my_" + tableInfo.getEntityName() + ".jsp";
        // }
        // });
        // cfg.setFileOutConfigList(focList);
        // mpg.setCfg(cfg);

        // 执行生成
        ag.execute();
    }
}
