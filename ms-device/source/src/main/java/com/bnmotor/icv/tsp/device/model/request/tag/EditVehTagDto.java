package com.bnmotor.icv.tsp.device.model.request.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: EditVehTagDto
 * @Description: 编辑车辆对应标签
 * @author: zhangwei2
 * @date: 2020/7/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ApiModel(value = "editVehTagDto", description = "编辑车辆对应的标签")
public class EditVehTagDto {
    @ApiModelProperty(value = "车架号")
    @NotEmpty(message = "车架号不能为空")
    private String vin;

    @Valid
    @ApiModelProperty(value = "分类信息列表")
    private List<CategoryDto> cats = new ArrayList<>();
}
