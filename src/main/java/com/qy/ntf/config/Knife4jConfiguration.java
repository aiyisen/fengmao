package com.qy.ntf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ProjectName: firstSet @Package: com.lingo.firstSet.config @ClassName:
 * SwaggerConfiguration @Author: 王振读 @Description: ${description} @Date: 2021/11/26 9:29 @Version:
 * 1.0
 */
@Configuration
@EnableSwagger2
public class Knife4jConfiguration {

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.qy.ntf.controller"))
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("API接口文档")
        .description("GENERAL为通用，APP 移动端接口，ADMIN 后台管理接口，无标识的接口可无需理会，其他的没啥可看的赶紧对接口吧，你一票我一票教主80还能跳")
        .termsOfServiceUrl("http://localhost:8090/")
        .contact("王振读")
        .version("1.0.0")
        .build();
  }
}
