package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: MockRemoteInstalledCallbackVo
 * @Description： 远程升级回调对象
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class MockRemoteInstalledCallbackVo {
    @ApiModelProperty(value = "立即安装异步回调消息", example = "", required = false)
    private InstalledVerifyResultVo installedVerifyResultVo;

    @ApiModelProperty(value = "预约安装异步回调消息", example = "", required = false)
    private InstalledBookedVerifyResultVo installedBookedVerifyResultVo;

    @ApiModelProperty(value = "前置条件检查异常异步回调消息", example = "", required = false)
    private RemoteInstalledCallbackVo preCheck;

    @ApiModelProperty(value = "取消升级异步回调消息", example = "", required = false)
    private RemoteInstalledCallbackVo cancel;

    @ApiModelProperty(value = "安装进度异步回调消息", example = "", required = false)
    private InstalledProcessVo installedProcessVo;

    @ApiModelProperty(value = "安装结果异步回调消息", example = "", required = false)
    private InstalledResultVo installedResultVo;
}
