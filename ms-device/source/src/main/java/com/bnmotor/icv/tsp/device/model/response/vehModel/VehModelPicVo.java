package com.bnmotor.icv.tsp.device.model.response.vehModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: VehModelPicVo
 * @Description: 车型图片
 * @author: zhangwei2
 * @date: 2020/11/16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehModelPicVo {
    @ApiModelProperty(value = "车型配置图片列表")
    private List<VehConfigPicVo> configs;

    @ApiModelProperty(value = "总配置数,用于进行分页处理")
    private Integer totalConfig;
}
