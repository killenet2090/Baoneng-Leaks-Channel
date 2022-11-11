package com.bnmotor.icv.tsp.sms.config;

import cn.jsms.api.JSMSClient;
import com.bnmotor.icv.tsp.sms.common.enums.ChannelEnum;
import com.bnmotor.icv.tsp.sms.config.convert.JsmsClientForMap;
import com.bnmotor.icv.tsp.sms.service.ISendSmsStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ApplicationConfig
 * @Description: 应用配置
 * @author: huangyun1
 * @date: 2020/5/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Configuration
@ConditionalOnBean(ApplicationPropertiesConfig.class)
@RefreshScope
public class ApplicationConfig {
    @Autowired
    private ApplicationPropertiesConfig applicationPropertiesConfig;
    @Resource
    private ISendSmsStrategy jsmsStrategyImpl;


    /**
     * 获取jsms客户端map
     * @return
     */
    @Bean
    public JsmsClientForMap<Integer> jsmsClientForMap() {
        JsmsClientForMap<Integer> jsmsClientForMap = new JsmsClientForMap();
        if(applicationPropertiesConfig.getJsmsSettingMap() != null && !applicationPropertiesConfig.getJsmsSettingMap().isEmpty()) {
            applicationPropertiesConfig.getJsmsSettingMap().stream().forEach(config -> {
                JSMSClient jPushClient = new JSMSClient(config.getMasterSecret(), config.getAppKey());
                jsmsClientForMap.put(config.getSendType(), jPushClient);
            });
        }
        return jsmsClientForMap;
    }

    /**
     * 组装发送短信策略
     * @return
     */
    @Bean
    public Map<Integer, ISendSmsStrategy> sendSmsStrategyServiceMap() {
        Map<Integer, ISendSmsStrategy> sendSmsStrategyMap = new HashMap<>(3);
        sendSmsStrategyMap.put(ChannelEnum.JSMS.getValue(), jsmsStrategyImpl);
        return sendSmsStrategyMap;
    }
}
