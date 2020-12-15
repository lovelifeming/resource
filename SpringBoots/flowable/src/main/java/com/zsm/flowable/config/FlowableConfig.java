package com.zsm.flowable.config;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-07 14:07.
 * @Description:
 */
@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration>
{
    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration)
    {
        engineConfiguration.setActivityFontName("宋体");
        engineConfiguration.setLabelFontName("宋体");
        engineConfiguration.setAnnotationFontName("宋体");
    }
}
