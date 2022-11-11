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
 * @ClassName: GeoDo
 * @Description: 地理位置关联关系表 实体类
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_geo_relationship")
@ApiModel(value="GeoRelationshipPo", description="地理位置信息表")
public class GeoRelationshipPo extends BasePo{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "祖先节点ID")
    private Long ancestor;

    @ApiModelProperty(value = "后代节点ID")
    private Long descendant;

    @ApiModelProperty(value = "祖先距离后代的距离")
    private Integer distance;

    @ApiModelProperty(value = "版本号")
    private Integer version;

}
