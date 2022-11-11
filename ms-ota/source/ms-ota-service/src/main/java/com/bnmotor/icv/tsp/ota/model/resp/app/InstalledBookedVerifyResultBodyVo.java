package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: InstalledBookedVerifyResultBodyVo
 * @Description： 安装确认结果消息主体
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class InstalledBookedVerifyResultBodyVo extends RemoteInstalledCallbackBodyVo{
    @ApiModelProperty(value = "1=HU端，2=APP端", example = "2", required = true)
    private Integer verifySource;

    @ApiModelProperty(value = "预约安装的时间", example = "123456789", required = true)
    private Long installedTime;

    @ApiModelProperty(value = "预计安装升级的时间", example = "123456789", required = true)
    private Long installedRemainTime;

}
