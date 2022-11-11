package com.bnmotor.icv.tsp.cpsp.engine.rules.engine.filter;


import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.annotation.RuleNameAnnotation;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.channel.ChannelDomain;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.channel.InstChannelApiDomain;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName: RuleFilter
 * @Description:
 * @author liuyiwei1
 * @date 2020-08-18 15:47
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public abstract class RulesFilter {

    public List<InstChannelApiDomain> channelApiList;

    public void execute(ChannelDomain domain) {
        channelApiList = domain.getInstChannelApis();
        RuleNameAnnotation ruleNameAnnotation = this.getClass().getAnnotation(RuleNameAnnotation.class);
//        if (channelApiList.isEmpty()) {
////            throw new AbortBizException("未找到匹配的路由规则");
//            throw new RuntimeException("未找到匹配的路由规则");
//        }

        filter(domain);
    }

    /**
     * 过滤路由规则
     * @param domain
     */
    protected abstract void filter(ChannelDomain domain);
}
