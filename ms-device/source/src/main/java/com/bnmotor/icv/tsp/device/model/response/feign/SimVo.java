package com.bnmotor.icv.tsp.device.model.response.feign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: SimVo
 * @Description: sim相关信息
 * @author: zhangwei2
 * @date: 2020/8/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class SimVo {
    @ApiModelProperty(value = "sim卡号")
    private String iccid;

    @ApiModelProperty(value = "移动设备识别码")
    private String imei;

    @ApiModelProperty(value = "国际移动用户识别码")
    private String imsi;

    @ApiModelProperty(value = "运营商")
    private String networkOperator;

    @ApiModelProperty(value = "运营商类型")
    private Integer operatorType;
}
