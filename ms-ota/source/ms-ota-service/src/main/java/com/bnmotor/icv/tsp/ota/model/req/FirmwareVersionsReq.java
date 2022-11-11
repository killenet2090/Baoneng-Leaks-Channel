package com.bnmotor.icv.tsp.ota.model.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: FirmwareVersionsReq
 * @Description:    post请求参数，用于固件相关操作请求
 * @author: xuxiaochang1
 * @date: 2020/6/5 13:13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class FirmwareVersionsReq {
    @ApiModelProperty(value = "项目Id", example = "guanzhi001", dataType = "String", required = true)
    private String projectId;

    @ApiModelProperty(value = "固件Id", example = "1000", dataType = "String", required = false)
    private String firmwareId;
}
