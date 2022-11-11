package com.bnmotor.icv.tsp.device.model.request.vehModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: ConfigPicDto
 * @Description: 配置图片查询
 * @author: zhangwei2
 * @date: 2020/11/17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class ConfigImgDto {
    @ApiModelProperty("车型id")
    private Long modelId;

    @ApiModelProperty(value = "年款id")
    private Long yearId;

    @ApiModelProperty(value = "配置id")
    private Long configId;

    @NotNull(message = "起始位置不能为空")
    @ApiModelProperty("起始configId;默认0，当进行翻页时，查询上一页返回list的最后一条配置的configId")
    private Long offset = 0L;

    @ApiModelProperty(value = "图片分类:1-外观;2-内饰;3-空间;4-官方;5-车主.默认1-外观")
    private Integer imgCategory = 1;

    @ApiModelProperty(value = "图片类型:1-缩略图;2-大图;默认1")
    private Integer imgType = 1;

    @ApiModelProperty(value = "每个配置查询多少张图片;默认9张")
    @Max(value = 9, message = "每个配置最大只能查询9张图片")
    private Integer picNum = 9;

    @ApiModelProperty(value = "每页条目;默认5条")
    @Max(value = 5, message = "每页最大只能查询5个配置图片")
    private Integer limit = 5;
}
