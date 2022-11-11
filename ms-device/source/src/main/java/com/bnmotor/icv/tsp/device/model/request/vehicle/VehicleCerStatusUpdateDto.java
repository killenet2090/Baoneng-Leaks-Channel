package com.bnmotor.icv.tsp.device.model.request.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: VehicleDto
 * @Description: 车辆设备信息新增请求DTO
 * @author: qiqi1
 * @date: 2020/8/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode
public class VehicleCerStatusUpdateDto implements Serializable {
    private static final long serialVersionUID = 3356815490149109147L;
    /**
     * 车架号
     */
    @NotEmpty(message = "车架号不能为空！")
    @ApiModelProperty("车架号")
    private String vin;

    /**
     * mno认证状态 0-未认证，1-已认证
     */
    @NotNull(message = "认证状态不能为空！")
    @ApiModelProperty("认证状态")
    private Integer certificationStatus;

    /**
     * old mno认证状态 0-未认证，1-已认证
     */
    @NotNull(message = "old 认证状态不能为空！")
    @ApiModelProperty("old认证状态")
    private Integer oldCertificationStatus;

    /**
     * 更新用户id
     */
    @ApiModelProperty("更新用户id")
    private Long updateUserId;

}
