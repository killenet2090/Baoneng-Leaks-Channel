package com.bnmotor.icv.tsp.cpsp;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import feign.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
* @ClassName: SmartHomeControlledApplication
* @Description: 智能家居-家控车
* @author: liuhuaqiao1
* @date: 2021/3/18
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@SpringBootApplication(scanBasePackages = {"com.bnmotor.icv.tsp.cpsp"})
@EnableFeignClients
@EnableSwaggerBootstrapUI
@MapperScan("com.bnmotor.icv.tsp.cpsp.mapper")
public class SmartHomeControlledApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartHomeControlledApplication.class, args);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}

