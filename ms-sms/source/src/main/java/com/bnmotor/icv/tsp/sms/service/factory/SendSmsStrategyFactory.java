package com.bnmotor.icv.tsp.sms.service.factory;

import com.bnmotor.icv.tsp.sms.model.request.AbstractSmsDto;
import com.bnmotor.icv.tsp.sms.service.ISendSmsStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName: FactoryForStrategy
 * @Description: 策略factory
 * @author: huangyun1
 * @date: 2020/5/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
public class SendSmsStrategyFactory {
    @Autowired
    Map<Integer, ISendSmsStrategy> sendSmsStrategyServiceMap;

    /**
     * 获取策略
     * @param abstractSmsDto
     * @return
     */
    public ISendSmsStrategy getStrategy(AbstractSmsDto abstractSmsDto) {
        ISendSmsStrategy strategy = sendSmsStrategyServiceMap.get(abstractSmsDto.getSendChannel());
        if(strategy == null) {
            throw new RuntimeException("no strategy defined");
        }
        return strategy;
    }
}
