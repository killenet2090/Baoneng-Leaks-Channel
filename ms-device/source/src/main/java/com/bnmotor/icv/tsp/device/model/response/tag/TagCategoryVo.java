package com.bnmotor.icv.tsp.device.model.response.tag;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: TagCategoryVo
 * @Description: 车辆标签分类
 * @author: zhangwei2
 * @date: 2020/8/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class TagCategoryVo {
    @ApiModelProperty("分类id")
    private String id;

    @ApiModelProperty("分类父id")
    private String parentId;

    @ApiModelProperty("分类名称")
    private String name;
}
