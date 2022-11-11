package com.bnmotor.icv.tsp.operation.veh.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @ClassName: VehicleModelVo
 * @Description:
 * @author: wuhao1
 * @data: 2020-07-17
 * @Copyrigght: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@EqualsAndHashCode
public class VehicleVo implements Serializable {

    private static final long serialVersionUID = 2710822052096550196L;
    @ApiModelProperty(value = "vehId")
    private Long id;

    @ApiModelProperty(value = "车辆唯一标识")
    private String vin;

    @ApiModelProperty(value = "车牌号码")
    private String drivingLicPlate;

    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "车辆状态")
    private Integer vehStatus;

    @ApiModelProperty(value = "车辆属性：对应多个标签")
    private String vehAttribute;

    @ApiModelProperty(value = "车主姓名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "车辆认证状态")
    private Integer certificationStatus;

    @ApiModelProperty(value = "车辆类型")
    private Integer vehType;

    @ApiModelProperty(value = "发布日期")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private String publishDate;

}
