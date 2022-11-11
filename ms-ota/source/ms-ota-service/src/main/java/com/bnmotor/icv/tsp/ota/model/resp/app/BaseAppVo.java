package com.bnmotor.icv.tsp.ota.model.resp.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: BaseAppVo
 * @Description： 响应给APP的对象基类
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:50
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class BaseAppVo<T> {
    @ApiModelProperty(value = "车辆Id", example = "bq001", required = true)
    private String vin;

    @ApiModelProperty(value = "响应时间戳", example = "123456789", required = true)
    private Long timestamp;

    @ApiModelProperty(value = "来自TBOX端生成的流水号", example = "100086", required = true)
    private Long businessId;

    @ApiModelProperty(value = "消息响应类型：1=远控，3=OTA云端", example = "3", required = true)
    private Integer type;

    @ApiModelProperty(value = "消息响应类型：1=升级确认(新版本检查结果推送），2=安装确认请求，3=远程下载结果异步结果推送，4=远程安装结果异步结果推送", example = "2", required = true)
    private Integer businessType;

    @ApiModelProperty(value = "消息主体", example = "", required = true)
    private T body;
}
