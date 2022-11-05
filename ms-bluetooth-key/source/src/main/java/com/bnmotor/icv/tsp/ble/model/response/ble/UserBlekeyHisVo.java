package com.bnmotor.icv.tsp.ble.model.response.ble;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserBlekeyHisVo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String projectId;
    /**
     *车辆ID
     */
    private String   deviceId;
    /**
     * 移动设备ID
     */
    private String  usedUserMobileDeviceId;
    /**
     * 移动设备型号
     */
    private String  usedUserMobileModel;
    /**
     * 移动设备号码
     */
    private String  usedUserMobileNo;
    /**
     * 使用者姓名
     */
    private String usedUserName;
    /**
     * 蓝牙钥匙ID
     */
    private String  bleKeyId;
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
     * 删除标志
     0 - 正常
     1 - 删除
     */
    private Integer delFlag;
    /**
     * 蓝牙钥匙生效时间
     */
    private String bleKeyEffectiveTime;
    /**
     * 蓝牙钥匙注销时间
     */
    private String bleKeyDestroyTime;
}
