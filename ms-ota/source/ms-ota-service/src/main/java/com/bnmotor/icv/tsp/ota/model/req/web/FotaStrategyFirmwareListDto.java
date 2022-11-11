package com.bnmotor.icv.tsp.ota.model.req.web;

import com.bnmotor.icv.tsp.ota.model.validate.Save;
import com.bnmotor.icv.tsp.ota.model.validate.Update;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: FotaStrategyFirmwareListDto
 * @Description: OTA升级策略-升级ecu固件列表 实体类
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@Accessors(chain = true)
public class FotaStrategyFirmwareListDto {
    @ApiModelProperty(value = "策略固件关联数据ID", example = "1000", dataType = "String")
    private String id;

    /**
     * 软件ID
     */
    @ApiModelProperty(value = "固件ID", example = "1000", dataType = "String", required = true)
    @NotNull(message = "固件ID不能为空", groups = {Save.class, Update.class})
    private String firmwareId;

    @ApiModelProperty(value = "固件名称", example = "测试TBox", dataType = "String")
    private String firmwareName;

    @ApiModelProperty(value = "固件代码", example = "测试TBox", dataType = "String")
    private String firmwareCode;

    @ApiModelProperty(value = "零件代码", example = "测试TBox", dataType = "String")
    private String componentCode;

    @ApiModelProperty(value = "零件名称", example = "测试TBox", dataType = "String")
    private String componentName;

    /**
     * 升级包模式； 1 - 差分升级包， 0 - 全量升级包
     */
    @ApiModelProperty(value = "升级包模式； 2=差分升级包， 1=补丁升级包(保留), 0=全量升级包", example = "1000", dataType = "Integer")
    @NotNull(message = "升级包模式不能为空", groups = {Save.class, Update.class})
    private Integer upgradeMode;

    /**
     * 目标版本Id
     */
    @ApiModelProperty(value = "目标版本Id", example = "1000", dataType = "String", required = true)
    @NotNull(message = "目标版本Id不能为空", groups = {Save.class, Update.class})
    private String targetVersionId;

    @ApiModelProperty(value = "目标版本名称", example = "1000", dataType = "String")
    private String targetVersion;

    /**
     * 升级顺序
     */
    @ApiModelProperty(value = "升级顺序，从1开始。服务器端维护", example = "1000", dataType = "Integer", required = false)
    private Integer orderNum;

    /**
     * 分组信息
     */
    @ApiModelProperty(value = "依赖分组信息,有依赖分组时必传：从1开始;保守策略或激进策略无依赖组时不需要传递", example = "1000", dataType = "Integer")
    private Integer groupSeq;
}
