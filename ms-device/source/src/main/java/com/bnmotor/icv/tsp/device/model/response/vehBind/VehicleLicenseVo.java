package com.bnmotor.icv.tsp.device.model.response.vehBind;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: TspDrivingLicensePo
 * @Description: 行驶证信息 实体类
 * @author huangyun1
 * @since 2020-09-24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@ApiModel(value = "行驶证返回对象", description = "行驶证返回对象")
public class VehicleLicenseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车辆识别代号 VIN
     */
    @ApiModelProperty(value = "车辆识别代号 VIN")
    private String vin;

    /**
     * 所有人
     */
    @ApiModelProperty(value = "所有人")
    private String userName;

    /**
     * 发动机号
     */
    @ApiModelProperty(value = "发动机号")
    private String engineNo;

    /**
     * 注册日期
     */
    @ApiModelProperty(value = "注册日期")
    private String registerDate;

    /**
     * 发证日期
     */
    @ApiModelProperty(value = "发证日期")
    private String issueDate;

    /**
     * 行驶证图片地址
     */
    @ApiModelProperty(value = "行驶证图片地址")
    private String imgUrl;

}
