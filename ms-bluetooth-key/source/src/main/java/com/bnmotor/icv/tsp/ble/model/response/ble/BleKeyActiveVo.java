package com.bnmotor.icv.tsp.ble.model.response.ble;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: BleKeyDelVo
 * @Description: 蓝牙钥匙注销json返回实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class BleKeyActiveVo implements Serializable {
    /**
     * 车辆ID
     */
    private String deviceId;
    /**
     * 蓝牙钥匙ID
     */
    private String bleKeyId;
    /**
     * 蓝牙钥匙状态
     0 - 未启用
     1 - 已启用
     2 - 已作废
     3 - 已过期
     4 - 需要细分，记录详细状态
     */
    private Integer bleKeyStatus;

}
