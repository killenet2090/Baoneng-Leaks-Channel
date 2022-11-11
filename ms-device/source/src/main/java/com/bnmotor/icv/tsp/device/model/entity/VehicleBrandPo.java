package com.bnmotor.icv.tsp.device.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: VehicleBrandPo
 * @Description: 用户安全问题答案 实体类
 * @author zhangwei2
 * @since 2020-07-04
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Accessors(chain = true)
@TableName("tb_vehicle_brand")
@ApiModel(value="VehicleBrandDo对象", description="用户安全问题答案")
public class VehicleBrandPo extends BasePo{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "项目代码")
    private String projectId;

    @ApiModelProperty(value = "代码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
