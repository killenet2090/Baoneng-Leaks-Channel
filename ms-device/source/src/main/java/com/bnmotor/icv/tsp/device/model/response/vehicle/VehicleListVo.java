package com.bnmotor.icv.tsp.device.model.response.vehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @ClassName: VehicleListVo
 * @Description: 车辆中心-车辆管理列表VO对象
 * @author: qiqi1
 * @date: 2020/8/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode
public class VehicleListVo implements Serializable {
    private static final long serialVersionUID = 7730624865394948021L;

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

    @ApiModelProperty(value = "车辆认证状态")
    private Integer certificationStatus;

    @ApiModelProperty(value = "车辆类型")
    private Integer vehType;

    @ApiModelProperty(value = "发布日期")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private String publishDate;

}
