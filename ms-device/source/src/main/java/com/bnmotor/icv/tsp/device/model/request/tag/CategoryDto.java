package com.bnmotor.icv.tsp.device.model.request.tag;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: CategoryDto
 * @Description: 标签组-分类DTO
 * @author: qiqi1
 * @date: 2020/8/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode
public class CategoryDto implements Serializable {
    private static final long serialVersionUID = 2162067904959033535L;

    @ApiModelProperty(value = "分类id")
    @NotNull(message = "分类id不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @Valid
    @ApiModelProperty(value = "标签信息列表")
    private List<TagDto> tags = new ArrayList<>();
}
