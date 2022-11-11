package com.bnmotor.icv.tsp.device.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: XxlJobConfig
 * @Description: xxljob配置
 * @author: zhangwei2
 * @date: 2020/10/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Configuration
@RefreshScope
public class XxlJobConfig {
    @Value("${xxl.job.admin.address}")
    private String adminAddress;
    @Value("${xxl.job.executor.appName}")
    private String appName;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddress);
        xxlJobSpringExecutor.setAppname(appName);
        return xxlJobSpringExecutor;
    }
}
