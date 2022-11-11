package com.bnmotor.icv.tsp.cpsp.engine.route.adapter.factory;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: StrategyFactory
 * @Description:
 * @author liuyiwei1
 * @date 2020-08-18 16:16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class StrategyFactory {

    /**
     * 策略集合: 一类业务为一个集合， 同类业务所有操作类型集中在这个集合中
     */
    private final Map<String, AbstractStrategy> strategyMap = new ConcurrentHashMap<>();

    @Autowired
    public StrategyFactory(Map<String, AbstractStrategy> strategyMap) {
        this.strategyMap.clear();

        strategyMap.forEach((k, v) -> {
            StringBuffer key = null;
            try {

                ThirdPartySvcAnno anno = v.getClass().getAnnotation(ThirdPartySvcAnno.class);
                key = new StringBuffer().append(anno.provider()).append(Constants.SEP).append(anno.service());
                this.strategyMap.put(key.toString(), v);
            } catch (Exception e) {
                log.info("Map injection error, key: {}", key);
                log.error("Map注入实例失败{}", key, e);
            }
        });
    }

    public AbstractStrategy getInstance(String key) {
        AbstractStrategy abstractStrategy = strategyMap.get(key);

        if (abstractStrategy == null) {
            throw new RuntimeException("无法获取当前策略, " + key);
        }
        return abstractStrategy;
    }
}
