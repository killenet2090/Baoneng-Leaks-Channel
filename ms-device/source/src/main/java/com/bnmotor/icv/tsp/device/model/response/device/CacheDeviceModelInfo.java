package com.bnmotor.icv.tsp.device.model.response.device;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: CacheDeviceModelInfo
 * @Description: 零件型号缓存
 * @author: zhangwei2
 * @date: 2020/11/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class CacheDeviceModelInfo {
    @ApiModelProperty(value = "自增id")
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
}
