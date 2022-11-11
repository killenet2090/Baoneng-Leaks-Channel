package com.bnmotor.icv.tsp.cpsp;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
* @ClassName: AutodecorationApplication
* @Description: 汽车美容微服务
* @author: liuhuaqiao1
* @date: 2021/1/14
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@SpringBootApplication(scanBasePackages = {"com.bnmotor.icv.tsp.cpsp"})
@EnableFeignClients
@EnableSwaggerBootstrapUI
public class AutodecorationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutodecorationApplication.class, args);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }

}
