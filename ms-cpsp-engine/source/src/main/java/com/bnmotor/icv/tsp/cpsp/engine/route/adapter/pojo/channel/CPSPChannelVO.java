package com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.channel;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: CPSPChannel
 * @Description:
 * @author liuyiwei1
 * @date 2020-08-26 11:50
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class CPSPChannelVO implements Serializable {
    private static final long serialVersionUID = -1913435559631938349L;

    /**
     * 业务类型， 如天气、违章
     */
    private String bizType;

    /**
     * 渠道编码
     */
    private String channelCode;

    /**
     * 渠道名， 如和风天气服务
     */
    private String channelName;



}
