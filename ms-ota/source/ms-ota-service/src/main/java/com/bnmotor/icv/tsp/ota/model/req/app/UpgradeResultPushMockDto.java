package com.bnmotor.icv.tsp.ota.model.req.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: UpgradeResultPushMockDto
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/9/14 15:49
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class UpgradeResultPushMockDto extends PushMockDto{
    @ApiModelProperty(value = "安装结果阶段：1=安装完成，2安装未完成，3安装失败", example = "1", required = true)
    private Integer upgradeResultType;
}
