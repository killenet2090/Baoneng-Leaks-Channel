package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: InstalledResultBodyVO
 * @Description： 前置条件检查消息体
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class InstalledResultBodyVo extends RemoteInstalledCallbackBodyVo{

    @ApiModelProperty(value = "安装结果：1=安装完成，2=安装未完成，3=安装失败", example = "1", required = true)
    private Integer  result;
}
