package com.bnmotor.icv.tsp.ota.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaComponentListPo
 * @Description: OTA升级硬件设备信息关系表 实体类
 * @author xuxiaochang1
 * @since 2020-11-05
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@Accessors(chain = true)
public class DeviceComponentVo {
    @ApiModelProperty(value = "显示名称", example = "TBOX", dataType = "string")
    /**
     * ID
     */
    private String id;

    @ApiModelProperty(value = "显示名称", example = "TBOX", dataType = "string")
    private String displayName;

    @ApiModelProperty(value = "零件代码区分不同零件的代号", example = "TBOX", dataType = "string")
    private String componentCode;

    @ApiModelProperty(value = "零件型号", example = "TBOX", dataType = "string")
    private String componentModel;

    @ApiModelProperty(value = "零件名称", example = "TBOX", dataType = "string")
    private String componentName;
}
