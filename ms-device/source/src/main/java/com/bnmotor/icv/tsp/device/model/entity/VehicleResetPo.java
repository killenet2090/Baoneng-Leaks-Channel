package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhangwei2
 * @ClassName: VehicleResetPo
 * @Description: 车辆复位信息 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Data
@Accessors(chain = true)
@TableName("tb_vehicle_reset")
@ApiModel(value = "VehicleResetDo对象", description = "车辆复位信息")
public class VehicleResetPo extends BasePo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "项目代码")
    private String projectId;

    @ApiModelProperty(value = "车架号")
    private String vin;

    @ApiModelProperty(value = "复位时间")
    private Date resetTime;

    @ApiModelProperty(value = "复位原因")
    private String resetReason;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
