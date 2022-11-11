package com.bnmotor.icv.tsp.device.model.request.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @ClassName: VehicleActivateDto
 * @Description: 车辆激活请求DTO
 * @author: qiqi1
 * @date: 2020/8/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode
public class VehicleActivateDto implements Serializable {
    private static final long serialVersionUID = 3356815490149109147L;

    /**
     * qrcodeKey令牌
     */
    @NotEmpty(message = "qrcodeKey令牌不能为空！")
    @ApiModelProperty("qrcodeKey令牌")
    private String qrcodeKey;
}
