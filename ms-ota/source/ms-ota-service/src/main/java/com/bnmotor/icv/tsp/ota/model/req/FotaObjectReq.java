package com.bnmotor.icv.tsp.ota.model.req;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: FotaObjectReq
 * @Description:
 * @author: handong1
 * @date: 2020/8/3 10:56
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ApiModel(description = "升级对象")
public class FotaObjectReq extends BasePo {

    @ApiModelProperty(value = "汽车VIN", example = "guanzhi001", dataType = "String")
    private String vin;

    @ApiModelProperty(value = "节点ID", example = "1278983692771991553", dataType = "Long")
    @JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
    private Long treeNodeId;

    @ApiModelProperty(value = "项目id,用于定义树的归属", example = "bngrp", dataType = "String")
    private String projectId;

    @ApiModelProperty(value = "每一颗树的根节点的id,如果自身是根节点,则为空", example = "1278883110929399810", dataType = "Long")
    private Long rootNodeId;

    @ApiModelProperty(value = "每一颗树的根节点的id,如果自身是根节点,则为空", example = "1278883235621863426", dataType = "Long")
    private Long parentId;

}
