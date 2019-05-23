package com.zsm.sb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * swagger2的配置文件，建在项目的启动类的同级目录下,访问路径：http://localhost:8080/swagger-ui.html
 *
 * @Api：修饰整个类，描述Controller的作用
 * @ApiOperation：描述一个类的一个方法，或者说一个接口
 * @ApiParam：单个参数描述
 * @ApiModel：用对象来接收参数
 * @ApiProperty：用对象接收参数时，描述对象的一个字段
 * @ApiResponse：HTTP响应其中1个描述
 * @ApiResponses：HTTP响应整体描述
 * @ApiIgnore：使用该注解忽略这个API
 * @ApiClass
 * @ApiError
 * @ApiErrors
 * @ApiParamImplicit
 * @ApiParamsImplicit
 * @-------------------------------
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/11.
 * @Modified By:
 */
@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "com.zsm.sb.controller")
public class Swagger2
{
    @Bean
    public Docket createdRestApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            //为当前包路径
            .apis(RequestHandlerSelectors.basePackage("com.zsm.sb.controller"))
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder()
            //页面标题
            .title("Spring Boot 测试 Swagger2 构建RESTful API文档")
            //创建人
            .contact(new Contact("zengsm", "", ""))
            //版本号
            .version("1.0.0")
            //描述
            .description("API文档描述")
            .build();
    }

    @Bean
    UiConfiguration uiConfig()
    {
        return new UiConfiguration(null, "list", "alpha", "schema",
            UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
    }
}
