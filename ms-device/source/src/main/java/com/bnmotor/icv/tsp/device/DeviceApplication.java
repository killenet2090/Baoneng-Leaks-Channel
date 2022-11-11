package com.bnmotor.icv.tsp.device;

import com.bnmotor.icv.tsp.device.common.ReqContextFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName: UserApplication
 * @Description: 用户服务启动boot
 * @author: zhangwei2
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DeviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeviceApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<ReqContextFilter> perssionFilterRegister() {
        FilterRegistrationBean<ReqContextFilter> registration = new FilterRegistrationBean();
        registration.setFilter(new ReqContextFilter()); //注册拦截器
        registration.addUrlPatterns("/*"); //拦截的URL
        registration.setName("reqContextFilter");
        registration.setOrder(1); //设置该拦截器执行的顺序
        return registration;
    }

}
