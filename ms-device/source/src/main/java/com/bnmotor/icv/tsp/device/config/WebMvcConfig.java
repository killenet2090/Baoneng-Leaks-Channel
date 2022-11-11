package com.bnmotor.icv.tsp.device.config;

import com.bnmotor.icv.tsp.device.common.AccessInterceptor;
import com.bnmotor.icv.tsp.device.common.UidAddInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessInterceptor()).addPathPatterns("/v1/**").excludePathPatterns("/mgt/**");
        registry.addInterceptor(new UidAddInterceptor()).addPathPatterns("/inner/**");
    }
}