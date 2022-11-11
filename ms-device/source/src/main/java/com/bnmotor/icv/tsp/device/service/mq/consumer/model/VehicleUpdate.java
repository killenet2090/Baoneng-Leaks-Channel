package com.bnmotor.icv.tsp.device.service.mq.consumer.model;

import lombok.Data;

/**
 * @ClassName: VehicleUpdate
 * @Description: 车辆更新消息
 * @author: zhangwei2
 * @date: 2020/11/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehicleUpdate {
    /**
     * 数据类型 1-org;2-vehicle;3-device
     */
    private Integer type;
    /**
     * 操作;1-插入;2-删除
     */
    private Integer action;

    private Long orgId;
}
