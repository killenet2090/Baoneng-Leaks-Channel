package com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: VehDeviceDelete
 * @Description: 车辆零部件解绑
 * @author: zhangwei2
 * @date: 2021/1/11
 * @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehDeviceDelete {
    /**
     * 车架号
     */
    private String vin;
    /**
     * 设备唯一标识符
     */
    private List<String> deviceIds;
    /**
     * 删除子类型
     */
    private Integer actionSubType;
}
