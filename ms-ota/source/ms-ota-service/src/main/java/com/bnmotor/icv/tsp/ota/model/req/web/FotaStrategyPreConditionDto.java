package com.bnmotor.icv.tsp.ota.model.req.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaStrategyPreConditionDto
 * @Description: OTA升级策略-前置条件
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@Accessors(chain = true)
public class FotaStrategyPreConditionDto {

    @ApiModelProperty(value = "条件名称", example = "电源状态", dataType = "String", required = true)
    private String condName;

    @ApiModelProperty(value = "条件代码", example = "1", dataType = "String", required = true)
    private String condCode;

    @ApiModelProperty(value = "条件操作类型:1=等于,2=大于,3=小于", example = "1", dataType = "Integer", required = true)
    private Integer operateType;

    @ApiModelProperty(value = "可选的条件值", example = "1", dataType = "Integer", required = true)
    Integer value;

    /*@ApiModelProperty(value = "可选的条件值", example = "1", dataType = "List", required = true)
    private List<PreConditionValue> preConditionValues;

    @Data
    @Builder
    public static class PreConditionValue{
        @ApiModelProperty(value = "是否未默认值", example = "1", dataType = "Integer", required = true)
        Integer isDefault;

        @ApiModelProperty(value = "可选的条件值", example = "1", dataType = "Integer", required = true)
        Integer value;

        @ApiModelProperty(value = "条件值说明", example = "1", dataType = "String", required = true)
        String valueDesc;
    }*/
}
