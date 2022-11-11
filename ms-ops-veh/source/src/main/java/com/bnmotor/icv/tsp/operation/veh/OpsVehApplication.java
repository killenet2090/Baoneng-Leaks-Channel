package com.bnmotor.icv.tsp.operation.veh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zhoulong1
 * @ClassName: OpsVehApplication
 * @Description: 启动类
 * @since: 2020/7/17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class OpsVehApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpsVehApplication.class, args);
    }

}
