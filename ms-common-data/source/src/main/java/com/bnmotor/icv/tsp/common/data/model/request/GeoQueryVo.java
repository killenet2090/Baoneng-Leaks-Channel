package com.bnmotor.icv.tsp.common.data.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @ClassName: GeoDo
 * @Description: 地理位置信息表 实体类
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@ApiModel(value="GeoQueryVo对象", description="地理位置查询对象")
public class GeoQueryVo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "地理位置—父ID")
    @Min(value = 0, message = "parentId参数最小值为0")
    private Long parentId;

    @ApiModelProperty(value = "地理位置—类型ID")
    @Min(value = 0, message = "typeId参数最小值为0")
    private Long typeId;

    @ApiModelProperty(value = "查询关键字")
    private String searchKey;

}
