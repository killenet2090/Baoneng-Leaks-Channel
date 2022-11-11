package com.bnmotor.icv.tsp.device.model.response.vehModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: PicVo
 * @Description: 图片实体
 * @author: zhangwei2
 * @date: 2020/11/16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class PicVo {
    @ApiModelProperty(value = "配置图片id")
    private String id;

    @ApiModelProperty(value = "图片路径")
    private String imgUrl;

    @ApiModelProperty(value = "图片排序")
    private Integer imgOrder;
}
