package com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn;

import lombok.Data;

/**
 * @ClassName: DataSysnMessage
 * @Description: 数据同步消息
 * @author: zhangwei2
 * @date: 2020/11/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class DataSysnMessage<T> {
    private Integer type;
    private Integer businessType;
    private Integer action;
    private T data;
}
