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
 * @ClassName: VehicleAsynQueryPo
 * @Description: 车辆异步查询表
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Data
@Accessors(chain = true)
@TableName("tb_veh_asyn_query")
@ApiModel(value = "VehicleAsynQueryPo", description = "车辆异步查询表")
public class VehicleAsynQueryPo extends BasePo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户uid")
    private Long uid;

    @ApiModelProperty(value = "查询类型1-configId;2-标签id")
    private Integer queryType;

    @ApiModelProperty(value = "查询值")
    private Long queryValue;

    @ApiModelProperty(value = "游标")
    private Long queryCursor;

    @ApiModelProperty(value = "状态值1-未执行；2-执行中;3-执行完成；4-执行失败")
    private Integer status;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
