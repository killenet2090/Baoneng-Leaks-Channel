package com.bnmotor.icv.tsp.ota.model.req.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName:  BaseAppDto
 * @Description: app客户端请求参数基类
 * @author: xuxiaochang1
 * @date: 2020/6/13 14:57
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class BaseAppDto {
    @ApiModelProperty(value = "车辆Vin码", example = "LLNC6ADB5JA047666", required = true)
    private String vin;

    @ApiModelProperty(value = "APP客户端设备类型:1=安卓,2=ios", example = "2", required = true)
    private Integer deviceType;

    private Integer isPush = 0;
}
