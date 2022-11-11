package com.bnmotor.icv.tsp.device.service.mq.consumer.model;

import lombok.Data;

/**
 * @ClassName: GuestUp
 * @Description: 临客上行数据
 * @author: zhangwei2
 * @date: 2020/11/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class GuestUp {
    /**
     * 车架号
     */
    private String vin;
    /**
     * 车辆状态：5-临客模式;51-临客模式开启中;52-临客模式关闭中
     */
    private Integer status;
}
