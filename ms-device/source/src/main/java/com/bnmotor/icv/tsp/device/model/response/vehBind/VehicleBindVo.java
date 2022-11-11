package com.bnmotor.icv.tsp.device.model.response.vehBind;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: VehicleBindVo
 * @Description: 车辆绑定返回实体 实体类
 * @author huangyun1
 * @since 2020-09-25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@ApiModel(value = "车辆绑定返回对象", description = "车辆绑定返回对象")
public class VehicleBindVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车主手机号
     */
    @ApiModelProperty(value = "车主手机号")
    private String phone;

}
