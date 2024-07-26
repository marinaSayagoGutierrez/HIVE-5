package com.hive5.utils.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hive5.utils.interceptors.AuthorizationInterceptor;

import jakarta.annotation.PostConstruct;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    @PostConstruct
    public void postConstruct() {
        System.out.println("WebConfig loaded");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("Registering interceptor");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/**").excludePathPatterns("/login");
    }
    
}
