package com.bnmotor.icv.tsp.apigateway.common.base;

/**
 * @ClassName: BaseEnum
 * @Description: 基础枚举类
 * @author: zhangjianghua1
 * @date: 2020/7/10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface BaseEnum<T> {
    /**
     * Enum field description
     * @return
     */
    String getDescription();

    /**
     * Enum identity value
     * @return
     */
    T getValue();
}
