package com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPOutput;

/**
 * @ClassName: AbstractStrategy
 * @Description:
 * @author liuyiwei1
 * @date 2020-08-18 15:47
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface AbstractStrategy<T extends CPSPInput<R>, R extends CPSPOutput> {

    /**
     * 调用策略
     * @param input
     * @return
     */
    R call(T input);
}
