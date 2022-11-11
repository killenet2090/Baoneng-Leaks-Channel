package com.bnmotor.icv.tsp.device.model.response.vehBind;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @ClassName: VehicleUserVo
 * @Description: 车辆用户信息
 * @author: huangyun1
 * @date: 2020/7/2
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@EqualsAndHashCode
public class VehicleUserVo {
    @ApiModelProperty(value = "用户uid")
    private String uid;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "车辆VIN码")
    private String vin;

    @ApiModelProperty(value = "用户身份证号")
    private String idCardNum;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "原因")
    private String reason;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "绑定时间")
    private String bindTime;

    @ApiModelProperty(value = "解绑时间")
    private String unbindTime;

    @ApiModelProperty(value = "是否实名认证")
    private String authFlag;

    @ApiModelProperty(value = "解绑渠道  1-APP、2-小程序、3-服务店、4-企业账号")
    private String unbindChannel;
}
