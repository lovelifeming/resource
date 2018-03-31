package com.zsm.ssmMG;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 逆向工程，自动生成mapper dao 实体类等
 * 命令启动：java -jar mybatis-generator-core-1.3.5.jar -MybatisGenerator.xml -overwrite
 *
 * @author zengsm
 */
public class MBGTest
{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MBGTest.class);

    public static void main(String[] args)
        throws Exception
    {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //配置MyBatis generator配置文件
        File configFile = new File("src/main/resources/MyBatisGenerator.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
