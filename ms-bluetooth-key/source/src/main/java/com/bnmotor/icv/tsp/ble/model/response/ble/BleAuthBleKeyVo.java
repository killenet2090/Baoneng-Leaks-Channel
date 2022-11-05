package com.bnmotor.icv.tsp.ble.model.response.ble;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: TspAuthBleKeyVo
 * @Description: 蓝牙钥匙确认返回json实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class BleAuthBleKeyVo {
    /**
     * 车辆ID
     */
    public String deviceId;

    /**
     * 蓝牙钥匙ID
     */
    public String bleKeyId;

    /**
     * 蓝牙钥匙名字
     */
    public String bleKeyName;

    /**
     * APP公钥加密的蓝牙钥匙密文
     */
    public String encryptAppBleKey;

    /**
     * 蓝牙钥匙云端私钥签名
     */
    public byte[] encryptAppBleKeySign;

    /**
     * 蓝牙连接名称
     */
    public String bleConName;

    /**
     * 蓝牙连接PIN码
     */
    public String bleConPin;

    /**
     * 蓝牙钥匙生效时间 YYYY-MM-DD hh:mm:ss
     */
    public Long bleKeyEffectiveTime;

    /**
     * 蓝牙钥匙失效时间（NULL表示永远有效） YYYY-MM-DD hh:mm:ss
     */
    public String bleKeyExpireTime;

    /**
     * 蓝牙钥匙更新时间 YYYY-MM-DD hh:mm:ss
     */
    public Long bleKeyRefreshTime;

    /**
     * 蓝牙钥匙状态：
     * 0 - 未启用
     * 1 - 已启用
     * 2 - 已作废
     * 3 - 已过期
     */
    public String bleKeyStatus;

    /**
     * 设备名字
     */
    public String deviceName;
    /**
     *设备类型
     */
    public String deviceModel;
    /**
     *蓝牙钥匙失效时间
     */
    public Long bleSkeyExpireTime;
    /**
     * 授权码超时时间
     */
    public Long authExpireTime;
    /**
     *公钥
     */
    public List<Map> authList;
    /**
     *授权凭证
     */
    private byte[] authVoucher;
    /**
     *授权凭证签名
     */
    private byte[] authVoucherSign;

    /**
     *车主姓名
     */
    private String createBy;

    /**
     * 测试用
     */
    public String byteSignSrc;

    /**
     * 测试用
     */
    private  byte[] byteAppBleKey;

    /**
     * 测试用
     */
    public String bleKeySrc;

    /**
     * 测试
     */
    private String blekeyByte;
}
