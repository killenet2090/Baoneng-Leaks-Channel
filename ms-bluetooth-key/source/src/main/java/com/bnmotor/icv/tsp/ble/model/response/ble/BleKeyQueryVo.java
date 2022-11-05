package com.bnmotor.icv.tsp.ble.model.response.ble;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: test
 * @Description: 描述类的作用
 * @author: liuyiwei
 * @date: 2020/7/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
public class BleKeyQueryVo implements Serializable {
    private static final long serialVersionUID = 998819215619010959L;
    /**
     * 车辆设备ID
     */
    private String deviceId;

    /**
     * 车辆设备名
     */
    private String deviceName;

    /**
     * 移动设备ID
     */
    private String mobileDeviceId;

    /**
     * 移动设备型号
     */
    private String usedUserMobileModel;

    /**
     * 蓝牙钥匙ID
     */
    private String bleKeyId;
    /**
     * 授权ID
     */
    private String bleAuthId;
    /**
     * 蓝牙钥匙名字
     */
    private String  bleKeyName;

    /**
     * 设备型号
     */
    private String deviceModel;

    /**
     * 蓝牙钥匙生效时间
     */
    private Long bleKeyEffectiveTime;

    /**
     * 蓝牙钥匙更新时间 YYYY-MM-DD hh:mm:ss
     */
    public Long bleKeyRefreshTime;

    /**
     * 蓝牙钥匙失效时间
     */
    private String bleKeyExpireTime;

    /**
     * 蓝牙钥匙状态
     * 0 - 未启用
     * 1 - 已启用
     * 2 - 已作废
     * 3 - 已过期
     * 4 - 需要细分，记录详细状态
     */
    private Integer bleKeyStatus;

    /**
     * 创建者（一般都是车主）
     */
    private String createBy;
}
