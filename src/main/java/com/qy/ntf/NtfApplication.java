package com.qy.ntf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;


@SpringBootApplication
@ConditionalOnClass(SpringfoxWebMvcConfiguration.class)
@MapperScan("com.qy.ntf.dao")
@EnableScheduling
public class NtfApplication implements WebMvcConfigurer {

  public static void main(String[] args) {
    SpringApplication.run(NtfApplication.class, args);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
    registry
        .addResourceHandler("/swagger-resources/**")
        .addResourceLocations("classpath:/META-INF/resources/swagger-resources/");
  }

  //    @Bean
  //    public CorsWebFilter corsFilter() {
  //        CorsConfiguration config = new CorsConfiguration();
  //        config.setAllowCredentials(true); // 允许cookies跨域
  //        config.addAllowedOrigin("*");//
  // #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
  //        config.addAllowedHeader("*");// #允许访问的头信息,*表示全部
  //        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
  //        config.addAllowedMethod("OPTIONS");// 允许提交请求的方法类型，*表示全部允许
  //        config.addAllowedMethod("HEAD");
  //        config.addAllowedMethod("GET");
  //        config.addAllowedMethod("PUT");
  //        config.addAllowedMethod("POST");
  //        config.addAllowedMethod("DELETE");
  //        config.addAllowedMethod("PATCH");
  //        org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource source =
  //                new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource(new
  // PathPatternParser());
  //        source.registerCorsConfiguration("/**", config);
  //        return new CorsWebFilter(source);
  //    }
}
