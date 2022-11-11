package com.bnmotor.icv.tsp.ota.model.resp.tbox;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: EcuFirmwareConfigListVo
 * @Description: ecu配置清单
 * @author: xuxiaochang1
 * @date: 2020/9/14 21:08
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class EcuFirmwareConfigListVo {
    
	@ApiModelProperty(value = "配置版本", example = "V1.0", required = true)
    private String confVersion;

    @ApiModelProperty(value = "配置清单列表", example = "", required = true)
    private List<EcuConfigVo> ecus;
}
