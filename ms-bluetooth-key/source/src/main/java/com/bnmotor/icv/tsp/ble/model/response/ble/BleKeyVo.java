package com.bnmotor.icv.tsp.ble.model.response.ble;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: TspBleKeyVo
 * @Description: 蓝牙钥匙json返回实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class BleKeyVo implements Serializable {
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
     * 用户姓名
     */
    public String userName;

    /**
     * 车辆名字
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
     * 移动设备名字
     */
    public String mobileDeviceName;

    /**
     * 加密的蓝牙钥匙
     */
    public String encryptAppBleKey;

    /**
     * 加密的蓝牙钥匙
     */
    public String encryptAppBleKeySign;

    /**
     * 蓝牙pin码
     */
    public String bleConPin;

    /**
     * 蓝牙pin码
     */
    public String bleConName;

    /**
     * 蓝牙钥匙生效时间
     */
    public Long bleKeyEffectiveTime;

    /**
     * 蓝牙钥匙失效时间
     */
    public Long bleSkeyExpireTime;

    /**
     * 蓝牙钥匙状态
     */
    public Integer bleKeyStatus;

    /**
     * 公钥
     */
    public String publicKey;

    /**
     * 权限列表
     */
    public String authList;

    /**
     * 蓝牙钥匙
     */
    public String bleKey;

    /**
     * 蓝牙钥匙ID
     */
    public String bleKeyId;

    /**
     * 蓝牙钥匙名字
     */
    private String  bleKeyName;
}
