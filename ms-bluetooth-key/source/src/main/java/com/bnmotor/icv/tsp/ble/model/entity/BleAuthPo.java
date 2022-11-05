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
 * @ClassName: TspBleAuth
 * @Description: 定义用户账号安全需要用到的一些安全问题 实体类
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
@TableName("tb_tsp_ble_auth")
public class BleAuthPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目ID
     */
    private String projectId;


//    /**
//     * 授权唯一ID
//     */
//    private Long bleAuthId;
    /**
     * 授权人用户ID
     */
    private String userId;


    /**
     * 用户别名，授权时输入
     */
    private String authedUserName;



    /**
      这里指车辆的ID
     */
    private String deviceId;

    /**
     这里指车辆的名字
     */
    private String deviceName;

    /**
     * 授权码
     */
    private String authCode;

    /**
     * 授权时间
     */
    private Date authTime;

    /**
     * 授权过期时间
            UTC时间毫秒数
     */
    private Long authExpireTime;
    /**
     * 蓝牙钥匙授权ID
     */
    private Long userAuthId;


    /**
     * 加密的App蓝牙钥匙
            被授权蓝牙钥匙采用被授权人App公钥加密
            在被授权人授权确认后，生成此信息
     */
    private String encryptAppBleKey;

    /**
     * 加密的App蓝牙钥匙签名
            在被授权人授权确认后，生成此信息
     */
    private String encryptAppBleKeySign;

    /**
     * 授权凭证
            采用设备端公钥加密后的凭证
     */
    private byte[] authVoucher;

    /**
     * 授权凭证签名
            采用平台蓝牙服务私钥签名
     */
    private byte[] authVoucherSign;

    /**
     * 被授权人手机号码
     */
    private String authedUserMobileNo;

    /**
     * 被授权人手机设备ID
     */
    private String authedMobileDeviceId;

    /**
     * 是否已接受授权
            0 - 新授权
            1 - 已确认
     */
    private Integer isAuthConfirmed;

    /**
     * 授权确认时间
     */
    private Date authConfirmedTime;

    /**
     * 授权确认用户id
     */
    private String authConfirmedUserId;

    /**
     * 用户类型
     */
    private Long userType;

    /**
     * 状态
            0 - 新申请
            1 - 已确认
            2 - 已取消
     */
    private Integer status;

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
     * 授权凭证过期时间
     */
    private String authVoucherExpireTime;


}
