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
 * @ClassName: TspDeviceBleInfo
 * @Description: 设备蓝牙信息 实体类
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
@TableName("tb_tsp_device_ble_info")
public class DeviceBleInfoPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 设备ID
            支持蓝牙钥匙的设备
            这里之车辆的ID
     */
    private String deviceId;

    /**
     * 设备Mac地址
     */
    private String deviceMac;

    private String productKey;

    private Integer deviceConfig;

    private String productCode;

    private String softwareVesion;

    /**
     * 状态
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

}
