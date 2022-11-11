package com.bnmotor.icv.tsp.cpsp;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import feign.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName: com.bnmotor.icv.tsp.cpsp.com.bnmotor.icv.tsp.cpsp.SmartHomeApplication
 * @Description: 智能家居
 * @author: jiangchangyuan1
 * @date: 2021/2/9
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@SpringBootApplication(scanBasePackages = {"com.bnmotor.icv.tsp.cpsp"})
@EnableFeignClients
@EnableSwaggerBootstrapUI
@MapperScan("com.bnmotor.icv.tsp.cpsp.mapper")
public class SmartHomeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartHomeApplication.class, args);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}

