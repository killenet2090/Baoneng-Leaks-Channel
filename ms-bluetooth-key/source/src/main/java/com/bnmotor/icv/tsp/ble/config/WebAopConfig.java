package com.bnmotor.icv.tsp.ble.config;

import com.bnmotor.icv.tsp.ble.common.BleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @ClassName:
 * @Description: 注册三器
 * @author: shuqi1
 * @date: 2020/5/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Configuration
public class WebAopConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BleInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        //resolvers.add(new GlobalExceptionResolver());
    }
}
