package com.bnmotor.icv.tsp.device.model.response.device;

import com.bnmotor.icv.adam.core.view.BaseView;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @ClassName: VehDeviceVo
 * @Description: 车型整车参数
 * @author: zhangwei2
 * @date: 2020/8/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@EqualsAndHashCode
public class ModelDeviceVo implements Serializable {
    private static final long serialVersionUID = -6584467228433296335L;
    /**
     * 零件ID
     */
    @JsonView(BaseView.class)
    private Long id;

    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @ApiModelProperty(value = "供应商")
    private String supplierName;
}
