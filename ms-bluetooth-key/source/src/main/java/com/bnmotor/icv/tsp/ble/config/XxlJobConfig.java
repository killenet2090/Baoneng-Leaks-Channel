package com.bnmotor.icv.tsp.ble.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: XxlJobConfig
 * @Description: xxl-jon配置类
 * @author: zhoulong1
 * @date: 2020/4/30
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Configuration
public class XxlJobConfig {
    private Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

    @Value("${xxl-job.admin.admin-addresses}")
    private String adminAddresses;

    @Value("${xxl-job.executor.app-name}")
    private String appname;

    @Value("${xxl-job.executor.port}")
    private int port;

    @Value("${xxl-job.executor.log-path}")
    private String logPath;

    @Value("${xxl-job.executor.log-retention-days}")
    private int logRetentionDays;


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        logger.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobSpringExecutor;
    }

}