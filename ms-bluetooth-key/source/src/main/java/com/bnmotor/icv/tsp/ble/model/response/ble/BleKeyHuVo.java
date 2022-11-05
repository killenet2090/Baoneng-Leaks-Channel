package com.bnmotor.icv.tsp.ble.model.response.ble;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: TspBleKeyVo
 * @Description: 蓝牙钥匙json返回实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class BleKeyHuVo implements Serializable {
    private static final long serialVersionUID = -2076143335872435498L;

    /**
     * 用户ID
     */
    public String userId;

    /**
     * 车辆ID
     */
    public String deviceId;

    /**
     * 蓝牙钥匙状态
     */
    public Integer bleKeyStatus;

    /**
     * 蓝牙钥匙ID
     */
    public String bleKeyId;

}
