package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: RemoteInstalledCallbackBodyVo
 * @Description： 远程安装结果
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class RemoteInstalledCallbackBodyVo extends BaseAppBodyVo{
    @ApiModelProperty(value = "安装阶段：1=立即安装确认指令下发确认结果，2=预约安装确认指令下发确认结果，3=前置条件检查异常，4=取消预约安装, 5=安装进度信息，6=安装结果", example = "1", required = true)
    private Integer installedProcessType;

    @ApiModelProperty(value = "前置条件检查失败异常信息列表", example = "1", required = true)
    private List<String> installedPrecheckFails;
}
