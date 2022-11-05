package com.bnmotor.icv.tsp.ble.model.response.ble;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: TspBleAuthVo
 * @Description: 蓝牙钥匙授权返回json实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class BleAuthVo {
    /**
     * 车辆设备ID
     */
    private String deviceId;
    /**
     * 被授权人手机号
     */
    private String phoneNumber;
    /**
     * 蓝牙钥匙生效时间（启始时间）
     */
    private Long bleKeyEffectiveTime;
    /**
     * 蓝牙钥匙失效时间，时间格式YYYY-MM-dd hh:mm:ss
     */
    private Long bleKeyExpireTime;
    /**
     * 授予的权限
     */
    private List<Long> authList;
    /**
     *用户类型：1-车主，2-家人，3-朋友，4-其他
     */
    private Integer userType;

    /**
     * 用户别名，授权时输入
     */
    private String usedUserName;
}
