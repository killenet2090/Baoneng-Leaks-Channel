package com.bnmotor.icv.tsp.ota.model.resp.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName: FotaStrategySelectableFirmwareVo
 * @Description: OTA升级策略-可选的固件列表
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@Accessors(chain = true)
public class FotaStrategySelectableFirmwareVo {
    /**
     * 固件ID
     */
    @ApiModelProperty(value = "固件ID", example = "1000", dataType = "String", required = true)
    private String firmwareId;

    /**
     * 固件名称
     */
    @ApiModelProperty(value = "固件代码", example = "1000", dataType = "String", required = true)
    private String firmwareCode;

    /**
     * 固件名称
     */
    @ApiModelProperty(value = "固件名称", example = "1000", dataType = "String", required = true)
    private String firmwareName;

    @ApiModelProperty(value = "零件类型", example = "cCode1", required = true)
    private String componentCode;

    @ApiModelProperty(value = "零件型号:同一类型的零件不同的型号", required = true)
    private String componentModel;

    @ApiModelProperty(value = "零件名称", required = true, example = "TCU")
    private String componentName;

    /**
     * 升级包模式； 2=差分升级包， 1=补丁升级包(保留), 0=全量升级包
     */
    @ApiModelProperty(value = "升级包模式； 2=差分升级包， 1=补丁升级包(保留), 0=全量升级包", example = "1000", dataType = "Integer")
    private List<Integer> upgradeModes;

    /**
     * 目标版本Id
     */
    @ApiModelProperty(value = "目标版本Id集合", dataType = "FirmwareVersionInfo", required = true)
    private List<FirmwareVersionInfo> targetVersions;

    /**
     * 版本信息
     */
    @Data
    public static class FirmwareVersionInfo{
        @ApiModelProperty(value = "固件版本", dataType = "String")
        private String targetVersionId;
        @ApiModelProperty(value = "固件版本名称", dataType = "String")
        private String targetVersionName;
        @ApiModelProperty(value = "固件版本刷新时长", dataType = "Integer")
        private Integer estimateUpgradeTime;
    }
}
