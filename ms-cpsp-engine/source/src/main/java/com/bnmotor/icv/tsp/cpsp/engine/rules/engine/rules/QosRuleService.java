package com.bnmotor.icv.tsp.cpsp.engine.rules.engine.rules;

import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.annotation.RuleNameAnnotation;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.filter.RulesFilter;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.channel.ChannelDomain;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @ClassName: QosRuleService
 * @Description:
 * @author liuyiwei1
 * @date 2020-09-09 9:16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@RuleNameAnnotation("Qos规则服务")
public class QosRuleService extends RulesFilter {
    @Override
    protected void filter(ChannelDomain domain) {
        //升序
        channelApiList = channelApiList.stream().sorted((s1, s2) -> s1.getQosRate().compareTo(s2.getQosRate())).collect(Collectors.toList());

        domain.setInstChannelApis(channelApiList);
    }
}
