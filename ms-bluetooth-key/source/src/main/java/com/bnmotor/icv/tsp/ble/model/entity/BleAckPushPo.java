package com.bnmotor.icv.tsp.ble.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: TspBleAckPush
 * @Description: 记录下发数据类型和流水号，用于消息推送 实体类
 * @author shuqi1
 * @since 2020-08-13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@Builder
@TableName("tb_tsp_ble_ack_push")
public class BleAckPushPo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 自增ID
     */
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 车辆ID
     */
    private String deviceId;

    /**
     * 蓝牙钥匙ID
     */
    private String blekeyId;

    /**
     * 权限ID
     */
    private String serviceId;

    /**
     * 车主ID
     */
     private String ownerUserId;

    /**
     * 使用者ID
     */
    private String usedUserId;

   /**
     * 生效时间
     */
    private Date effectiveTime;

    /**
     * 超时时间
     */
    private Long expireDate;

    /**
     * 加密的蓝牙钥匙
     */
    private String encryptAppBleKey;

    /**
     * 签名
     */
    private String encryptAppBleKeySign;

    /**
     * 授权凭证
     */
    private String authVoucher;

    /**
     * 授权凭证签名
     */
    private String authVoucherSign;

    /**
     * app的注册ID
     */
    private String registrationId;

    /**
     * pin码
     */
    private String pinCode;


    /**
     * 用户类型
     */
    private Long userTypeId;



    /**
     * 流水号
     */
    private Long serialNum;




    /**
     * 命令类型
     */
    private Integer cmdType;

    /**
     * 命令号
     */
    private Integer cmdOrder;

    /**
     * 状态
     */
    private Integer status;

    /**
     * MQTT返回的状态
     */
    private String ackStatus;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 删除标志
     */
    private Integer delFag;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;


}
