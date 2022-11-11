package com.bnmotor.icv.tsp.sms.config;

import com.bnmotor.icv.tsp.sms.config.convert.JsmsProperties;
import com.bnmotor.icv.tsp.sms.config.convert.TemplateProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: PushPropertiesConfig
 * @Description: 推送配置类
 * @author: huangyun1
 * @date: 2020/5/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@RefreshScope
@Data
@ConfigurationProperties(prefix = "tsp.sms")
public class ApplicationPropertiesConfig {
    /**
     * 极光短信配置列表
     */
    private List<JsmsProperties> jsmsSettingMap;

    /**
     * 模板配置列表
     */
    private Map<String, TemplateProperties> templateSettingMap;

    /**
     * 映射模板id前缀
     */
    private String mappingTemplateIdPrefix;


}
