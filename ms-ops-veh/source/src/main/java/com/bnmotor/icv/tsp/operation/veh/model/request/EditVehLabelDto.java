package com.bnmotor.icv.tsp.operation.veh.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @ClassName: EditVehLabelDto
 * @Description: 编辑车辆对应标签备注
 * @author: zhoulong1
 * @date: 2020/7/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ApiModel(value = "editVehLabelDto", description = "编辑车辆对应的标签")
public class EditVehLabelDto {

    @NotBlank(message = "vin不能为空")
    @Length(min = 10, max = 32, message = "vin长度非法")
    @ApiModelProperty(value = "车辆唯一标识", name = "vin", required = true)
    private String vin;

    @NotEmpty(message = "标签id不能为空")
    @Size(max = 10,message = "大于最大允许设置的标签数")
    @ApiModelProperty(value = "标签id集合", name = "labels", required = true)
    private List<Long> labels;

    @Length(min = 2, max = 100, message = "note长度不符合")
    @ApiModelProperty(value = "备注", name = "note")
    private String remark;

    @Length(min = 2, max = 100, message = "更新人")
    @ApiModelProperty(value = "更新人", name = "updateBy")
    private String updateBy;
}
