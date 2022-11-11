package com.bnmotor.icv.tsp.ota.model.req.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: RemoteInstalledDto
 * @Description： 远程安装确认
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class GetTboxUpgradStatusDto{
    @ApiModelProperty(value = "车辆vin码", example = "1000086", required = false)
    private String vin;
}
