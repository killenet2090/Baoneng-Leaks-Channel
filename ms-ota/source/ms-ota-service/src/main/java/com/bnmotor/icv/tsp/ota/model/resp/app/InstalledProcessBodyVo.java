package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: InstalledProcessBodyVO
 * @Description： 安装进度消息体
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class InstalledProcessBodyVo extends RemoteInstalledCallbackBodyVo{
    @ApiModelProperty(value = "总计包数量", example = "8", required = true)
    private Integer installedTotalNum;

    @ApiModelProperty(value = "当前上传为第几个包", example = "123456789", required = true)
    private Integer installedCurrentIndex;

    @ApiModelProperty(value = "当前正在安装包的进度:1-100", example = "50", required = true)
    private Integer installedPercentRate;

    @ApiModelProperty(value = "当前正在安装ECU名称", example = "TBOX_MCU", required = true)
    private String installedEcuName;

    @ApiModelProperty(value = "当前正在安装固件Id", example = "TBOX_MCU", required = true)
    private Long installedFirmwareId;

    @ApiModelProperty(value = "安装预估剩余时间", example = "1000", required = true)
    private Integer installedRemainTime;

    @ApiModelProperty(value = "总计包数量", example = "8", required = true)
    private Integer totalPkgNum;

    @ApiModelProperty(value = "当前上传为第几个包", example = "123456789", required = true)
    private Integer currentPkgIndex;
}
