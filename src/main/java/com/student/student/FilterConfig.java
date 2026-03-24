package com.student.student;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthFilter> registerFilter(AuthFilter filter) {

        FilterRegistrationBean<AuthFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(filter);

        bean.addUrlPatterns("/*"); // 🔥 apply to all URLs

        return bean;
    }
}