package com.bnmotor.icv.tsp.ota.model.req.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @ClassName: FotaDeviceComponentInfoDto
 * @Description: 设备树节点信息
 * @author: xuxiaochang1
 * @date: 2020/10/27 10:50
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ApiModel(value="车型零件同步信息对象", description="车型零件同步信息对象")
public class FotaDeviceComponentInfoDto implements Serializable {
    private static final long serialVersionUID = 4600580160910285958L;

    @ApiModelProperty(value = "项目id,用于定义树的归属", example = "guanzhi001")
    private String projectId;

    @ApiModelProperty(value = "组织机构Id")
    private Long orgId;

    /*@ApiModelProperty(value = "序列号")
    private String sn;*/

    /*@ApiModelProperty(value = "旧的零件号")
    private String oldSn;*/

    @ApiModelProperty(value = "品牌")
    @NotEmpty(message = "品牌不能为空")
    private String brand;

    @ApiModelProperty(value = "品牌代码")
    private String brandCode;

    @ApiModelProperty(value = "车系")
    @NotEmpty(message = "车系不能为空")
    private String series;

    @ApiModelProperty(value = "车系代码")
    private String seriesCode;

    @ApiModelProperty(value = "车型")
    @NotEmpty(message = "车型不能为空")
    private String model;

    @ApiModelProperty(value = "车型代码")
    private String modelCode;

    @ApiModelProperty(value = "年款")
    @NotEmpty(message = "年款不能为空")
    private String year;

    @ApiModelProperty(value = "年款代码")
    private String yearCode;

    @ApiModelProperty(value = "配置")
    @NotEmpty(message = "配置不能为空")
    private String conf;

    @ApiModelProperty(value = "配置代码")
    private String confCode;

    @ApiModelProperty(value = "零件类型")
    @NotEmpty(message = "零件类型不能为空")
    private String componentType;

    @ApiModelProperty(value = "零件类型名称")
    @NotEmpty(message = "零件类型名称不能为空")
    private String componentTypeName;

    @ApiModelProperty(value = "零件代码区分不同零件的代号")
    private String componentCode;

    @ApiModelProperty(value = "零件型号")
    @NotEmpty(message = "零件型号不能为空")
    private String componentModel;

    @ApiModelProperty(value = "零件名称")
    @NotEmpty(message = "配置")
    private String componentName;

    private Integer action;

    /*@ApiModelProperty(value = "软件版本")
    private String softwareVersionNo;

    @ApiModelProperty(value = "硬件版本")
    private String hardwareVersionNo;*/
}
