package com.bnmotor.icv.tsp.vehstatus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName: VehStatusApplication
 * @Description: 微服务启动类
 * @author: huangyun1
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class VehStatusApplication {
    public static void main(String[] args) {
        SpringApplication.run(VehStatusApplication.class, args);
    }

}
