package com.bnmotor.icv.tsp.cpsp;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName: DateUtils
 * @Description: 天气微服务启动类
 * @author: liuyiwei1
 * @date: 2020/8/17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@SpringBootApplication(scanBasePackages = {"com.bnmotor.icv.tsp.cpsp"})
@EnableFeignClients
@EnableSwaggerBootstrapUI
public class VehviolationApplication {
    public static void main(String[] args) {
        SpringApplication.run(VehviolationApplication.class, args);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }

}
