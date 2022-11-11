package com.bnmotor.icv.tsp.ota.model.req.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: PushMockDto
 * @Description:  模拟请求参数
 * @author: xuxiaochang1
 * @date: 2020/8/19 11:37
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class PushMockDto {
    @ApiModelProperty(value = "车辆vin码", example = "LLNC6ADB5JA047666", required = true)
    private String vin;

    @ApiModelProperty(value = "设备Id,用于推送", example = "xidibeidiid810", required = true)
    private String deviceId;

    @ApiModelProperty(value = "APP设备类型", example = "1", required = true)
    private Integer deviceType;

    @ApiModelProperty(value = "业务消息类型", example = "1", required = true)
    private Integer businessType;

    /*@ApiModelProperty(value = "是否需要异步等待结果：1=需要，2=不需要", example = "1", required = true)
    private Integer isAsync;*/

    /*@ApiModelProperty(value = "任务Id", example = "100086L", required = true)
    private Long taskId;

    @ApiModelProperty(value = "事务Id", example = "100086L", required = true)
    private Long transId;*/
}
