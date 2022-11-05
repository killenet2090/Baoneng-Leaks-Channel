package com.bnmotor.icv.tsp.ble.model.response.ble;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: TspBleAuthVo
 * @Description: 蓝牙钥匙授权返回json实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class BleAuthingVo {
    /**
     *用户ID
     */
    public String userId ;
    /**
     *用户ID
     */
    public String userName ;

    /**
     *车辆设备ID
     */
    public String deviceId;

    /**
     *车辆设备名字
     */
    public String deviceName;
    /**
     *授权码开始时间
     */
    public Long authTime;

    /**
     *授权码过期时间
     */
    public Long authExpireTime;
    /**
     *授权凭证过期时间
     */
    public  Long authVoucherExpireTime;


}
