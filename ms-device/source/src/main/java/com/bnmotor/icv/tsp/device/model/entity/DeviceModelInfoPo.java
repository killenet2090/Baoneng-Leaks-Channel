package com.bnmotor.icv.tsp.device.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: DeviceModelInfoDo
 * @Description: 定义不同设备型号的相关信息 实体类
 * @author HP
 * @since 2020-08-12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_device_model_info")
@ApiModel(value="DeviceModelInfoDo对象", description="定义不同设备型号的相关信息")
public class DeviceModelInfoPo extends BasePo{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(value = "项目代码")
    private String projectId;

    @ApiModelProperty(value = "零件型号")
    private String deviceModel;

    @ApiModelProperty(value = "零件中午名称")
    private String chiName;

    @ApiModelProperty(value = "零件英文名称")
    private String engName;

    @ApiModelProperty(value = "零件英文缩写")
    private String ngNameShort;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "ECU硬件版本号")
    private String hardwareVersion;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
