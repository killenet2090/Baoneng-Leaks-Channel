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
 * @author zhangwei2
 * @ClassName: VehicleOrgRelationPo
 * @Description: 车辆层级结构
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_vehicle_org_relation")
@ApiModel(value = "VehicleOrgRelationPo对象", description = "车辆层次结构")
public class VehicleOrgRelationPo extends BasePo {
    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "项目代码")
    private String projectId;

    @ApiModelProperty(value = "品牌")
    private Long brandId;

    @ApiModelProperty(value = "品牌编码")
    private String brandCode;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "车系")
    private Long vehSeriesId;

    @ApiModelProperty(value = "车系编码")
    private String seriesCode;

    @ApiModelProperty(value = "车系名称")
    private String vehSeriesName;

    @ApiModelProperty(value = "车型")
    private Long vehModelId;

    @ApiModelProperty(value = "车型编码")
    private String modelCode;

    @ApiModelProperty(value = "车型名称")
    private String vehModelName;

    @ApiModelProperty(value = "年款")
    private Long yearStyle;

    @ApiModelProperty(value = "年款编码")
    private String yearCode;

    @ApiModelProperty(value = "年款名称")
    private String yearStyleName;

    @ApiModelProperty(value = "配置")
    private Long configurationId;

    @ApiModelProperty(value = "配置编码")
    private String configCode;

    @ApiModelProperty(value = "配置名称")
    private String configName;

    @ApiModelProperty(value = "车辆类型")
    private Integer vehType;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
