package com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.channel;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName: InstChannelApiDomain
 * @Description:
 * @author liuyiwei1
 * @date 2020-08-18 15:49
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class InstChannelApiDomain implements Serializable {

    private static final long serialVersionUID = -1839012247519432627L;

    /**
     * 渠道API编码
     */
    private String gatewayChannelApi;

    /**
     * 渠道API
     */
    private String gatewayChannelApiDesc;


    /**
     * 渠道状态
     */
    private String status;

    /**
     * 是否默认渠道
     */
    private Boolean defaultChannel;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * qos分数
     */
    private BigDecimal qosRate;

    /**
     * 流量权重
     */
    private Integer weight;

    /**
     * 渠道服务提供商标识ID
     */
    private Long serviceProviderId;
}
