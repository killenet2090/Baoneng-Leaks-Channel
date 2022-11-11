package com.bnmotor.icv.tsp.ota.model.req.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: InstalledProcessPushMockDo
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/9/14 15:49
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class InstalledProcessPushMockDo extends PushMockDto{
    /*
     INSTALLED_PRECHECK_FAIL(1, "前置条件检查失败"),
    INSTALLED_CANCEL_FROM_HU(3, "取消升级"),
    INSTALLED_PROCESSING(2, "执⾏升级(表示⻋辆具备升级条件，正式进⼊升级环节)"),
     */
    @ApiModelProperty(value = "安装异步回调类型：1=立即安装确认指令下发确认结果，2=预约安装确认指令下发确认结果，3=前置条件检查异常，4=取消安装（来自Hu）, 5=安装进度信息，6=安装结果", example = "1", required = true)
    private Integer installedProcessType;

    @ApiModelProperty(value = "安装结果类型：1=安装成功，2=安装未完成，3=安装失败", example = "1", required = true)
    private Integer installedResult;

    @ApiModelProperty(value = "错误码", example = "1", required = true)
    private Integer errorCode;

    @ApiModelProperty(value = "错误信息", example = "1", required = true)
    private String errorMsg;
}
