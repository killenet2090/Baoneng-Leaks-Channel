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
public class RemoteInstalledDto extends BaseAppDto{
    @ApiModelProperty(value = "APP定义异步交互的事物Id(一般为TBOX定义)", example = "1000086", required = false)
    private String businessId;

    @ApiModelProperty(value = "事务Id", example = "1000086", required = true)
    private Long transId;

    @ApiModelProperty(value = "任务Id", example = "1000086", required = true)
    private Long taskId;

    @ApiModelProperty(value = "安装确认类型：1=立即升级，2=预约升级", example = "2", required = true)
    private Integer installedType;

    @ApiModelProperty(value = "安装确认时间，格式为时间戳", example = "100086", required = true)
    private Long installedTime;

    @ApiModelProperty(value = "安装确认类型：1=升级，2=取消升级。默认为1", example = "1", required = true)
    private Integer verifyResult;
}
