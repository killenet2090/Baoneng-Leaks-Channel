package com.bnmotor.icv.tsp.vehicle.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName: MqttAuthApplication
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: wuhao1
 * @date: 2020/12/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@EnableFeignClients
@SpringBootApplication
public class MqttAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqttAuthApplication.class);
    }
}
