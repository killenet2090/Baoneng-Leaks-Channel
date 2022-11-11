package com.bnmotor.icv.tsp.device.model.response.vehModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: VehModelYearVo
 * @Description: 车型年款响应
 * @author: zhangwei2
 * @date: 2020/11/17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehModelImgVo {
    @ApiModelProperty("车型id")
    private String id;

    @ApiModelProperty(value = "车型名称")
    private String modelName;

    @ApiModelProperty(value = "图片地址")
    private String imgUrl;
}
