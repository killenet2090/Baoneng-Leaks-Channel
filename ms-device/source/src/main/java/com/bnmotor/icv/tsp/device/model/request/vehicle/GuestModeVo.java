package com.bnmotor.icv.tsp.device.model.request.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: GuestModeVo
 * @Description: 临客模式设置实体
 * @author: zhangwei2
 * @date: 2020/11/16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class GuestModeVo {
    @ApiModelProperty(value = "车架号")
    @NotBlank(message = "车架号不能为空")
    private String vin;
}
