package gaea.user.center.service.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.management.ManagementFactory;
import java.net.URL;

/**
 * @ClassName: Log4j2Config
 * @Description: TODO(暂时先采用该方式获取系统变量，log4j2 xml文件对占位符的支持不够友好，后期根据elk完善)
 * @author: jiankang
 * @date: 2020/4/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
@Component
public class Log4j2Config implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${adam.logging.path}")
    private String loggingPath;

    @Value("${adam.logging.config}")
    private String loggingConfig;

    private static String logPath;
    private static String applicationName;
    private static String loggerConfig;



    @PostConstruct
    private void init(){
        logPath = this.loggingPath;
        applicationName = this.appName;
        loggerConfig = this.loggingConfig;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.setProperty("logPath", logPath);
        System.setProperty("appName", applicationName);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        System.setProperty("pid", pid);
        System.out.println(loggerConfig);
        String str = loggerConfig.substring(loggerConfig.indexOf(":")+1, loggerConfig.length());
        String log4jPropertiesPath = this.getClass().getResource("/") + str;
        try {
            URL url = new URL(log4jPropertiesPath);
            ConfigurationSource source = new ConfigurationSource(url.openStream(), url);
            LoggerContext context = Configurator.initialize(null, source);
            XmlConfiguration xmlConfig = new XmlConfiguration(context, source);
            context.start(xmlConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
