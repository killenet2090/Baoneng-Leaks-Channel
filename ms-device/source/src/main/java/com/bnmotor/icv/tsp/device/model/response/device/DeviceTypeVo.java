package com.bnmotor.icv.tsp.device.model.response.device;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: DeviceTypeVo
 * @Description: 设备类型信息
 * @author: zhangwei2
 * @date: 2020/8/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class DeviceTypeVo {
    @ApiModelProperty("设备类型id")
    private Long id;

    @ApiModelProperty("设备类型")
    private Integer deviceType;

    @ApiModelProperty("类型名称")
    private String typeName;

    @ApiModelProperty("设备扩展信息")
    private String deviceExt;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
