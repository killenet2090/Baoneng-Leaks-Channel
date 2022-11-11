package com.bnmotor.icv.tsp.cpsp.engine.rules.engine.context;

import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.annotation.RuleNameAnnotation;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.strategy.RulesStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: RulesStrategyContext
 * @Description:
 * @author liuyiwei1
 * @date 2020-08-26 10:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class RulesStrategyContext {

    private final Map<String, RulesStrategy> rulesStrategyMap = new ConcurrentHashMap<>();

    @Autowired
    public RulesStrategyContext(Map<String, RulesStrategy> rulesStrategyMapAutoWired) {
        rulesStrategyMap.clear();

        rulesStrategyMapAutoWired.forEach((k, v) -> {
           String key = v.getClass().getAnnotation(RuleNameAnnotation.class).value();
           rulesStrategyMap.put(key, v);
        });
    }

    public RulesStrategy getRulesStrategy(String strategy){
        return rulesStrategyMap.get(strategy);
    }
}
