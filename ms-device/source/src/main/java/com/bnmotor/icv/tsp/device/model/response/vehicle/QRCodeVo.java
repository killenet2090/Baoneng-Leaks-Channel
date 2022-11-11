package com.bnmotor.icv.tsp.device.model.response.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: VehicleQRCodeScanVo
 * @Description: 车辆信激活二维码扫码返回实体
 * @author: huangyun1
 * @date: 2020/7/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@EqualsAndHashCode
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QRCodeVo {

    @ApiModelProperty(value = "二维码状态：1-正常 2-已失效 3-已激活")
    private Integer qrCodeState;

    /**
     *访问令牌
     */
    @ApiModelProperty(value = "访问令牌")
    private String accessToken;

    /**
     * 刷新令牌
     */
    @ApiModelProperty(value = "刷新令牌")
    private String refreshToken;

    /**
     * qrcodeKey令牌
     */
    @ApiModelProperty(value = "qrcodeKey令牌")
    private String qrcodeKey;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

}
