package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author HP
 * @ClassName: VehModelDeviceDo
 * @Description: 车型对应零部件型号 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_veh_model_device")
@ApiModel(value = "VehModelDeviceDo对象", description = "车型对应零部件型号")
public class VehModelDevicePo extends BasePo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "项目代码")
    private Long projectId;

    @ApiModelProperty(value = "组织id")
    private Long orgId;

    @ApiModelProperty(value = "品牌id")
    private Long brandId;

    @ApiModelProperty(value = "车系id")
    private Long seriesId;

    @ApiModelProperty(value = "车型id")
    private Long modelId;

    @ApiModelProperty(value = "年款id")
    private Long yearStyleId;

    @ApiModelProperty(value = "配置ID")
    private Long configId;

    @ApiModelProperty(value = "零件型号")
    private String deviceModel;

    @ApiModelProperty(value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
