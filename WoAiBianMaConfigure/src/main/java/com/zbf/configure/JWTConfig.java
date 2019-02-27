/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: JWTConfig
 * Author:   gyr
 * Date:     2019/1/28 15:33
 * Description:
 */
package com.zbf.configure;

import com.zbf.filter.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;

/**
 * JWT配置类：
 *      用于加载JWT的过滤器JWTFilter
 */
@Configuration
public class JWTConfig {

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public FilterRegistrationBean jwtFilter(){
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        //添加自定义的JWT过滤器
        registrationBean.setFilter(new JWTFilter(redisTemplate));

        //为JWT过滤器添加拦截规则
        registrationBean.addUrlPatterns("/menu/*","/loginout","/tiku2/*","tiku/*");

        return registrationBean;
    }

}