package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author HP
 * @ClassName: DeviceReplacementPo
 * @Description: 车辆ECU信息
 * 定义车辆装配的ECU，来源于车辆分类树ECU清单，或者后续的换件更新 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Data
@Accessors(chain = true)
@TableName("tb_device_replacement")
@ApiModel(value = "DeviceReplacementDo对象", description = "车辆ECU信息定义车辆装配的ECU，来源于车辆分类树ECU清单，或者后续的换件更新")
public class DeviceReplacementPo extends BasePo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "项目代码")
    private String projectId;

    @ApiModelProperty(value = "车架号")
    private String vin;

    @ApiModelProperty(value = "设备类型 0 - TBox 1 - 车机 2 - sim卡 3 = ECU")
    private Integer deviceType;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "设备信息ID")
    private Long deviceModelInfoId;

    @ApiModelProperty(value = "硬件版本")
    private String hardwareVersion;

    @ApiModelProperty(value = "软件版本")
    private String softwareVersion;

    @ApiModelProperty(value = "更换时间")
    private LocalDateTime bindTime;

    @ApiModelProperty(value = "更换原因")
    private String replaceReason;

    @ApiModelProperty(value = "更换渠道说明")
    private String replaceChannel;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
