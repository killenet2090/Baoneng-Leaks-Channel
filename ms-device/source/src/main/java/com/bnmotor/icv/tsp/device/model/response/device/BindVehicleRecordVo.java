package com.bnmotor.icv.tsp.device.model.response.device;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName: BindVehicleRecordVo
 * @Description: 零部件绑车记录
 * @author: zhangwei2
 * @date: 2020/8/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class BindVehicleRecordVo {
    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "车架号")
    private String vin;

    @ApiModelProperty(value = "绑定时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bindTime;

    @ApiModelProperty(value = "解绑时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime unbindTime;

    @JsonIgnore
    private LocalDateTime createTime;
}
