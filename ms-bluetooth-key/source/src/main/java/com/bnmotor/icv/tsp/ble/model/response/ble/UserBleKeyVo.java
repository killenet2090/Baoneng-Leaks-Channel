package com.bnmotor.icv.tsp.ble.model.response.ble;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: UserBleKey
 * @Description: 用户蓝牙钥匙
记录用户申请到的蓝牙钥匙 实体类
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class UserBleKeyVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     *车辆ID
     */
    private String   deviceId;
    /**
     * 移动设备ID
     */
    private String  usedUserMobileDeviceId;
    /**
     * 设备名字
     */
    private String  deviceName;
    /**
     * 设备型号
     */
    private String  deviceModel;
    /**
     * 移动设备型号
     */
    private String  usedUserMobileModel;
    /**
     * 移动设备号码
     */
    private String  usedUserMobileNo;
    /**
     *是否确认
     */
    private Integer isAuthConfirmed;
    /**
     * 确认时间
     */
    private Date authConfirmedTime;
    /**
     * 使用者姓名
     */
    private String usedUserName;
    /**
     * 蓝牙钥匙ID
     */
    private String  bleKeyId;
    /**
     * 授权ID
     */
    private String bleAuthId;
    /**
     * 蓝牙钥匙名字
     */
    private String  bleKeyName;
    /**
     * 确认时间
     */
    private String bleKeyExpireTime;
    /**
     * 蓝牙钥匙状态
     */
    private Integer bleKeyStatus;
    /**
     * 权限列表
     */
    private Map authList;

    /**
     * 加密的蓝牙钥匙
     采用使用者App端公钥加密
     */
    private String encryptAppBleKey;

    /**
     * 蓝牙钥匙发放时间
     */
    private String bleKeyIssueTime;

    /**
     * 蓝牙钥匙生效时间
     */
    private long bleKeyEffectiveTime;

    /**
     * 蓝牙钥匙刷新时间
     */
    private String bleKeyRefreshTime;

    /**
     * 蓝牙钥匙注销时间
     */
    private String bleKeyDestroyTime;

    /**
     * 蓝牙钥匙使用模式
     0 - 工厂模式
     1 - 车展模式
     2 - 用户模式
     */
    private Integer bleWorkModel;


    /**
     * 用户类型
     */
    private Long userType;

    /**
     * 创建者（一般都是车主）
     */
    private String createBy;


}
