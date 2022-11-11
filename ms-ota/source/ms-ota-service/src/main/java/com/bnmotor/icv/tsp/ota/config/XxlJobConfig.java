package com.bnmotor.icv.tsp.ota.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: XxlJobConfig
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/10/20 10:15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Configuration
@Slf4j
public class XxlJobConfig {
    @Value("${xxl-job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl-job.executor.app-name}")
    private String appname;

    @Value("${xxl-job.executor.ip:}")
    private String ip;

    @Value("${xxl-job.executor.port}")
    private int port;

    @Value("${xxl-job.executor.log-path}")
    private String logPath;

    @Value("${xxl-job.executor.log-retention-days}")
    private int logRetentionDays;


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        /*xxlJobSpringExecutor.setAddress(address);*/
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        /*xxlJobSpringExecutor.setAccessToken(accessToken);*/
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        log.info("adminAddresses={}, appname={}, ip={}, port={}, logPath={}, logRetentionDays={}", adminAddresses, appname, ip, port, logPath, logRetentionDays);
        return xxlJobSpringExecutor;
    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     *      1、引入依赖：
     *          <dependency>
     *             <groupId>org.springframework.cloud</groupId>
     *             <artifactId>spring-cloud-commons</artifactId>
     *             <version>${version}</version>
     *         </dependency>
     *
     *      2、配置文件，或者容器启动变量
     *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     *      3、获取IP
     *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */
}