package com.bnmotor.icv.tsp.ble.model.response.ble;

import com.bnmotor.icv.tsp.ble.model.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: BleKeyModifyVo
 * @Description: 蓝牙钥匙通用修改json返回实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class BleKeyModifyVo extends Model {
    /**
     * 用户ID
     */
    public String userId ;

    /**
     * 车辆ID
     */
    public String deviceId;

    /**
     * 用户名字
     */
    public String userName;

    /**
     * 车牌号
     */
    public String deviceName;

    /**
     * 车辆型号
     */
    public String deviceModel;

    /**
     * 移动设备ID
     */
    public String mobileDeviceId;

    /**
     * 移动设备型号
     */
    public String usedUserMobileModel;

    /**
     * 移动设备号码
     */
    public String usedUserMobileNo;

    /**
     * 蓝牙钥匙生效时间
     */
    public Long bleKeyEffectiveTime;

    /**
     * 蓝牙钥匙生效时间
     */
    public Long bleSkeyExpireTime;

    /**
     * 蓝牙钥匙状态
     */
    public Integer bleKeyStatus;

    /**
     * 授权列表
     */
    public String authList;

}
