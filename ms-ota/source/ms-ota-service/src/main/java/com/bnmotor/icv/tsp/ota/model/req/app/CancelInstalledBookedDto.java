package com.bnmotor.icv.tsp.ota.model.req.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName:  CancelInstalledBookedDto
 * @Description: app客户端请求参数-取消预约安装
 * @author: xuxiaochang1
 * @date: 2020/6/13 14:57
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class CancelInstalledBookedDto extends BaseAppDto{
    @ApiModelProperty(value = "升级事务Id", example = "1000086", required = false)
    private Long transId;

    @ApiModelProperty(value = "任务Id", example = "1000086", required = true)
    private Long taskId;
}
