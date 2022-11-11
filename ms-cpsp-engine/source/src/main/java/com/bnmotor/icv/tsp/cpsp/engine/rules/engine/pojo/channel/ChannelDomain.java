package com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.channel;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: ChannelDomain
 * @Description:
 * @author liuyiwei1
 * @date 2020-08-18 15:49
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class ChannelDomain implements Serializable {

    private static final long serialVersionUID = 5298083260466764255L;

    /**
     * 渠道目标提供商
     */
    private Long serviceProviderId;

    /**
     * 路由的渠道集合
     */
    private List<InstChannelApiDomain> instChannelApis;
}
