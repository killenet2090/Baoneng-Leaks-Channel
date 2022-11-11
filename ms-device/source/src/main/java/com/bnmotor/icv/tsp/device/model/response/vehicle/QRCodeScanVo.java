package com.bnmotor.icv.tsp.device.model.response.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: ScanQRCodeVo
 * @Description: 扫描二维码返回信息
 * @author: huangyun1
 * @date: 2021/1/9
 * @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class QRCodeScanVo implements Serializable {
    /**
     * 扫码确认连接
     */
    @ApiModelProperty(value = "扫码确认连接", name = "qrConfirm", required = true, example = "xxx")
    private String scanConfirm;

    /**
     * 扫码取消链接
     */
    @ApiModelProperty(value = "扫码取消链接", name = "qrCancel", required = true, example = "tyyy")
    private String scanCancel;
}
