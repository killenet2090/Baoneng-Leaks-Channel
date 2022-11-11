package com.bnmotor.icv.tsp.apigateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @ClassName: ApiGatewayProperties
 * @Description: 网关属性配置类
 * @author: huangyun1
 * @date: 2020/5/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@ConfigurationProperties(prefix = "api.gateway")
@RefreshScope
@Data
public class ApiGatewayProperties {
    /**
     * token鉴权验证开关
     */
    private Boolean checkToken = true;

    /**
     * 始终放行
     */
    private Set<String> permitAll;

    /**
     * token鉴权验证忽略
     */
    private Set<String> tokenIgnores;

    /**
     * projectId
     */
    private String projectId;

}
