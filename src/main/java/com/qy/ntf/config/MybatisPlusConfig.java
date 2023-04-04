package com.qy.ntf.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: firstSet
 * @Package: com.lingo.firstSet.config
 * @ClassName: MybatisPlusConfig
 * @Author: 王振读
 * @Description: ${description}
 * @Date: 2021/12/16 13:47
 * @Version: 1.0
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {

        // mybatis plus 3.4, use MybatisPlusInterceptor.
        //System.out.println("**************** 分页了 **********************");
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
