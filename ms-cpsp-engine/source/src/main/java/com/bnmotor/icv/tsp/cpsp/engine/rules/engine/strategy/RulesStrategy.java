package com.bnmotor.icv.tsp.cpsp.engine.rules.engine.strategy;

import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.annotation.RuleNameAnnotation;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.constant.Constants;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.biz.BaseDomain;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.channel.ChannelDomain;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.rules.DefaultRuleService;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.rules.PriorityRuleService;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.rules.QosRuleService;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.strategy.base.AbstractRuleStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: RulesStrategy
 * @Description: 全局默认规则策略， 可以实现其他策略达到各种规则的组合
 * @author liuyiwei1
 * @date 2020-08-25 17:30
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@RuleNameAnnotation(Constants.STRATEGY_DEFAULT)
public class RulesStrategy extends AbstractRuleStrategy {

    @Autowired
    private DefaultRuleService defaultRuleService;

    @Autowired
    private PriorityRuleService priorityRuleService;

    @Autowired
    private QosRuleService qosRuleService;

    @Override
    protected void buildChannel(ChannelDomain channelDomain, BaseDomain domain) {

    }

    @Override
    protected void process(ChannelDomain domain) {
        //默认渠道规则
        defaultRuleService.execute(domain);

        //优先级规则
        priorityRuleService.execute(domain);

        //qos故障分低优先
        qosRuleService.execute(domain);
    }
}
