package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: UpgradeStatusBaseInfoVo
 * @Description:    下载中需要携带的信息
 * @author: xuxiaochang1
 * @date: 2020/8/19 14:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
public class UpgradeStatusBaseInfoVo {
    @ApiModelProperty(value = "当前版本", example = "1", required = true)
    private String currentVersion;

    @ApiModelProperty(value = "最新目标版本", example = "", required = true)
    private String targetVersion;
}
