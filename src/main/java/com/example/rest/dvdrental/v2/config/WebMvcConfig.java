package com.example.rest.dvdrental.v2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String redirectUrl = "/app/";
        registry.addViewController("/app/").setViewName("forward:/app/index.html");
        registry.addRedirectViewController("/", redirectUrl);
        registry.addRedirectViewController("/app", redirectUrl);
//        registry.addRedirectViewController("/app/**", redirectUrl);
    }
}
