package com.bnmotor.icv.tsp.cpsp.engine.rules.engine.strategy.base;

import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.channel.ChannelDomain;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.biz.BaseDomain;

/**
 * @ClassName: AbstractRuleStrategy
 * @Description:
 * @author liuyiwei1
 * @date 2020-08-25 17:31
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public abstract class AbstractRuleStrategy {

    public void call(ChannelDomain channelDomain, BaseDomain domain){
        process(channelDomain);//处理规则
        buildChannel(channelDomain,domain);//构建渠道
    }

    /**
     * 渠道构建
     * @param channelDomain
     * @param domain
     */
    protected abstract void buildChannel(ChannelDomain channelDomain, BaseDomain domain);

    /**
     * 执行策略
     * @param domain
     */
    protected abstract void process(ChannelDomain domain);
}
