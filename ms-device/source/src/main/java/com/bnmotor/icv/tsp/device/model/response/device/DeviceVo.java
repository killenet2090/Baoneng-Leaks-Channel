package com.bnmotor.icv.tsp.device.model.response.device;

import com.bnmotor.icv.adam.core.view.BaseView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: VehDeviceVo
 * @Description: 车辆设备信息
 * @author: zhangwei2
 * @date: 2020/8/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class DeviceVo implements Serializable {
    private static final long serialVersionUID = -6584467228433296335L;
    /**
     * 零件ID
     */
    @JsonView(BaseView.class)
    private String id;

    /**
     * 零件ID
     */
    @JsonIgnore
    @JsonView(BaseView.class)
    private String deviceId;

    @ApiModelProperty(value = "生产序列号")
    @JsonView(BaseView.class)
    private String productSn;

    @ApiModelProperty(value = "车牌号码")
    private String drivingLicPlate;

    @ApiModelProperty(value = "车架号")
    private String vin;

    @JsonIgnore
    @ApiModelProperty(value = "设备类型")
    @JsonView(BaseView.class)
    private Integer deviceType;

    @ApiModelProperty(value = "设备类型名称")
    @JsonView(BaseView.class)
    private String deviceTypeName;

    @ApiModelProperty(value = "设备型号")
    @JsonView(BaseView.class)
    private String deviceModel;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "换件次数")
    private Integer replaceTimes;

    @ApiModelProperty(value = "激活状态")
    private Integer enrollStatus;
}
