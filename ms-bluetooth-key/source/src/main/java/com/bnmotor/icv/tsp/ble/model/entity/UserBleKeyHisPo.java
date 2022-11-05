package com.bnmotor.icv.tsp.ble.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: UserBleKey
 * @Description: 用户蓝牙钥匙
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
@TableName("tb_user_ble_key_his")
public class UserBleKeyHisPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 蓝牙钥匙设备ID
     支持蓝牙钥匙的设备
     这里之车辆的ID
     */
    private String deviceId;
    /**
     * 蓝牙钥匙ID
     */
    private String bleKeyId;
    /**
     * 蓝牙钥匙名字
     */
    private String bleKeyName;

    /**
     * 授权唯一ID
     */
    private Long bleAuthId;

    /**
     * 蓝牙钥匙所属用户
     */
    private String ownerUserId;
    /**
     * 蓝牙钥匙使用用户
     对于车主，使用用户就是自己
     */
    private String usedUserId;

    /**
     * 蓝牙钥匙设备名称
            用于蓝牙钥匙的设备的人眼识别
            存车辆的车牌号
     */
    private String deviceName;

    /**
     * 设备型号
     */
    private String deviceModel;


    /**
     * 蓝牙钥匙使用用户移动设备ID
     */
    private String usedUserMobileDeviceId;

    /**
     * 蓝牙钥匙使用用户手机号码
     */
    private String usedUserMobileNo;

    /**
     * 蓝牙钥匙使用用户手机型号
     */
    private String usedUserMobileModel;

    /**
     * 加密的蓝牙钥匙
            采用使用者App端公钥加密
     */
    private String encryptAppBleKey;

    /**
     * 蓝牙钥匙发放时间
     */
    private Date bleKeyIssueTime;

    /**
     * 蓝牙钥匙生效时间
     */
    private Date bleKeyEffectiveTime;

    /**
     * 蓝牙钥匙到期时间
     */
    private Long bleKeyExpireTime;

    /**
     * 蓝牙钥匙刷新时间
     */
    private Date bleKeyRefreshTime;

    /**
     * 蓝牙钥匙注销时间
     */
    private Date bleKeyDestroyTime;

    /**
     * 蓝牙钥匙状态
            0 - 未启用
            1 - 已启用
            2 - 已作废
            3 - 已过期
            4 - 需要细分，记录详细状态
     */
    private Integer bleKeyStatus;

    /**
     * 蓝牙钥匙使用模式
            0 - 工厂模式
            1 - 车展模式
            2 - 用户模式
     */
    private Integer bleWorkModel;

    /**
     * 删除标志
            0 - 正常
            1 - 删除
     */
    private Integer delFlag;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建人
            登录帐号
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
            登录的帐号
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 用户类型
     */
    private Long userType;

    /**
     * 用户名字
     */
    private String usedUserName;

}
