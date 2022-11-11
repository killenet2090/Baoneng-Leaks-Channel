package com.bnmotor.icv.tsp.cpsp.engine.rules.engine.rules;

import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.annotation.RuleNameAnnotation;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.filter.RulesFilter;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.channel.ChannelDomain;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.channel.InstChannelApiDomain;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: DefaultRuleService
 * @Description: 默认渠道规则服务
 * @author liuyiwei1
 * @date 2020-09-07 15:41
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@RuleNameAnnotation("默认优先规则服务")
public class DefaultRuleService extends RulesFilter {

    @Override
    protected void filter(ChannelDomain domain) {
        //支持多个默认渠道， 移除非默认渠道
        List<InstChannelApiDomain> tmp = channelApiList.stream().filter(s -> s.getDefaultChannel() ).collect(Collectors.toList());

        //若未设置默认渠道， 则本规则不生效
        if(tmp.size() > 0){
            domain.setInstChannelApis(tmp);
        }
    }
}
