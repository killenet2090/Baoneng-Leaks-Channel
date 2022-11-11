package com.bnmotor.icv.tsp.device.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: EnviromentHelper
 * @Description: 当前应用环境帮助类，主要用户获取应用一些通用配置
 * @author: zhangwei2
 * @date: 2020/5/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class Enviroment {
    @Value("${spring.profiles.active}")
    private String profiles;

    @Value("${spring.application.name}")
    private String appName;

    /**
     * 判断当前环境是否是开发环境
     */
    public boolean isDev() {
        return "dev".endsWith(profiles);
    }

    /**
     * 判断当前环境是否是测试环境
     */
    public boolean isTest() {
        return "test".endsWith(profiles);
    }

    /**
     * 判断当前环境是否是测试环境
     */
    public boolean isPro() {
        return "pro".endsWith(profiles);
    }

    /**
     * 获取app
     */
    public String getAppName() {
        return appName;
    }
}
