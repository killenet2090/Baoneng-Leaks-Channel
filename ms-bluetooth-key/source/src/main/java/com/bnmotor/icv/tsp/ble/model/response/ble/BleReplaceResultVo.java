package com.bnmotor.icv.tsp.ble.model.response.ble;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: BleReplaceResultVo
 * @Description: 蓝牙钥匙更新后json返回实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class BleReplaceResultVo {
    /**
     * 车辆ID
     */
    private String deviceId;
    /**
     * 加密的蓝牙钥匙
     */
    private String encryptAppBleKey;
    /**
     * pin连接名字
     */
    private String bleConName;
    /**
     * pin 码
     */
    private String bleConPin;
    /**
     * app端签名
     */
    private byte[] encryptAppBleKeySign;
    /**
     * 蓝牙钥匙开始时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long bleKeyEffectiveTime;
    /**
     * 蓝牙钥匙失效时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long blekeyExpireTime;
    /**
     * 蓝牙钥匙更新时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long bleKeyRefreshTime;
    /**
     * 蓝牙钥匙状态
     */
    private String bleKeyStatus;
}
