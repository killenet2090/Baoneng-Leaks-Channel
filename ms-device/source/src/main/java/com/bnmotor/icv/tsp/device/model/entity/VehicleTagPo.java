package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhangwei2
 * @ClassName: VehicleLabelPo
 * @Description: 车辆标签关系维护 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Data
@Accessors(chain = true)
@TableName("tb_vehicle_tag")
@ApiModel(value = "VehicleTagPo", description = "车辆标签关系维护")
public class VehicleTagPo extends BasePo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "车架号")
    private String vin;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "标签id")
    private Long tagId;

    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "备注")
    private String remark;
}
