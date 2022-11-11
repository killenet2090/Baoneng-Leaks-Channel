package com.bnmotor.icv.tsp.device.model.request.tag;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: VehicleTagDto
 * @Description: 车辆打标DTO
 * @author: qiqi1
 * @date: 2020/8/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode
public class TagDto implements Serializable {
    private static final long serialVersionUID = 8957183377285984215L;

    @ApiModelProperty(value = "标签id")
    @NotNull(message = "标签id不能为空")
    private Long tagId;

    @ApiModelProperty(value = "标签名称")
    private String tagName;

}
