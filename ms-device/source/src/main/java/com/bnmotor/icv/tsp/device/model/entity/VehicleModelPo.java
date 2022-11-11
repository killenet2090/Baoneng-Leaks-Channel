package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhangwei2
 * @ClassName: VehicleModelPo
 * @Description: 车型 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Data
@Accessors(chain = true)
@TableName("tb_vehicle_model")
@ApiModel(value = "VehicleModelDo对象", description = "车型")
public class VehicleModelPo extends BasePo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "项目代码")
    private String projectId;

    @ApiModelProperty(value = "所属车系")
    private Long seriesId;

    @ApiModelProperty(value = "代码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "车辆级别")
    private String vehLevel;

    @ApiModelProperty(value = "车辆类别 1-SUV;2-轿车;3-MPV")
    private Integer vehCategory;

    @ApiModelProperty(value = "车辆类型,1-燃油，2-纯电动,3-混动")
    private Integer vehType;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
