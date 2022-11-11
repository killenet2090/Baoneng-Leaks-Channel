package com.bnmotor.icv.tsp.cpsp.engine.rules.engine.rules;

import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.annotation.RuleNameAnnotation;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.filter.RulesFilter;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.channel.ChannelDomain;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @ClassName: PriorityRuleService
 * @Description:
 * @author liuyiwei1
 * @date 2020-08-18 15:47
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@RuleNameAnnotation("优先级排序规则服务")
public class PriorityRuleService extends RulesFilter {
    @Override
    public void filter(ChannelDomain domain) {
        //升序
        channelApiList = channelApiList.stream().sorted((s1, s2) -> s1.getPriority().intValue() - s2.getPriority().intValue()).collect(Collectors.toList());

        domain.setInstChannelApis(channelApiList);
    }
}
