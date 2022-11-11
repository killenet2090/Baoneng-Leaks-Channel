package com.bnmotor.icv.tsp.common.data.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: GeoTypeDo
 * @Description: 地理位置区域类型 实体类
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_geo_type")
@ApiModel(value="GeoTypeDo对象", description="地理位置区域类型")
public class GeoTypePo extends BasePo {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "类型名称")
    private String name;

    @ApiModelProperty(value = "类型编码")
    private String code;

    @ApiModelProperty(value = "区域级别")
    private Integer level;

    @ApiModelProperty(value = "版本号")
    private Integer version;

}
