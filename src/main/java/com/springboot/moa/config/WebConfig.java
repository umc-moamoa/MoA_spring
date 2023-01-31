package com.springboot.moa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://15.165.168.76:9000", "http://127.0.0.1:8080", "http://seolmunzip.shop:9000", "http://127.0.0.1:9000")
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .maxAge(3600);
    }
}