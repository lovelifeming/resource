package com.zsm.apidoc;

import com.zsm.apidoc.apijson.util.ModelCache;
import com.zsm.apidoc.model.UserConst;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * swagger文档
 */
@Configuration
@EnableSwagger2
public class Swagger2
{
    private static final String URL = null;   //http://localhost:8080/swagger-ui.html#/";

    private static final String BASE_PACKAGE = "com.zsm.apidoc.controller";

    @Bean
    public Docket docket()
    {
        ModelCache.getInstance().setParamClass(UserConst.class);

        return new Docket(DocumentationType.SWAGGER_2).groupName("Swagger接口文档")
            .apiInfo(new ApiInfoBuilder().title("Swagger接口文档")
                .contact(new Contact("APIJson", URL, null))
                .version("1.0").build())
            .select().paths(PathSelectors.any())
            .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
            .build();
    }

    @Bean
    UiConfiguration uiConfig()
    {
        return new UiConfiguration(null, "list", "alpha", "schema",
            UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
    }

}
