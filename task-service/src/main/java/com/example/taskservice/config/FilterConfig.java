
package com.example.taskservice.config;

import com.example.taskservice.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired private JwtFilter jwtFilter;

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration() {
        FilterRegistrationBean<JwtFilter> reg = new FilterRegistrationBean<>(jwtFilter);
        reg.addUrlPatterns("/tasks/*","/tasks"); // protect endpoints
        reg.setOrder(1);
        return reg;
    }
}
