package com.bnmotor.icv.tsp.ble;

import com.bnmotor.icv.tsp.ble.util.RandomUnit;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName: DateUtils
 * @Description: 微服务提供方
 * @author: shuqi1
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@MapperScan("com.bnmotor.icv.tsp.ble.mapper")
@SpringBootApplication
@EnableFeignClients
@EnableSwaggerBootstrapUI
public class BleApplication {
    public static void main(String[] args) {SpringApplication.run(BleApplication.class, args); }

}
